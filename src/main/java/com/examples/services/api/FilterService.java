package com.examples.services.api;

import com.examples.dto.SearchCriteria;
import com.examples.dto.SearchCriteriaForClass;
import com.examples.dto.SearchCriteriaValuesRange;
import com.examples.enums.OperationSign;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public interface FilterService {

    Map<String, Class<?>> cachedMetamodelClasses = new HashMap<>();

    String getModelPackageName();

    default TypedQuery<?> createCriteriaQuery(final EntityManager entityManager,
                                              final SearchCriteriaForClass searchCriteriaForClass) {

        Class<?> searchedClass = getMetaModelClass(searchCriteriaForClass.getFilteredClass());
        if (searchedClass != null) {
            final Class<?> referenceModelClass = extractModelReferenceClass(searchedClass);
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<?> criteriaQuery = criteriaBuilder.createQuery(referenceModelClass);
            Root<?> root = criteriaQuery.from(referenceModelClass);
            if (searchCriteriaForClass.getSearchCriteria() != null) {
                List<Predicate> predicates = getPredicates(root, searchCriteriaForClass.getSearchCriteria(), criteriaBuilder);
                criteriaQuery.where(predicates.toArray(Predicate[]::new));
            }
            if (searchCriteriaForClass.getAscending()) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(searchCriteriaForClass.getSortBy())));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(searchCriteriaForClass.getSortBy())));
            }

            return entityManager.createQuery(criteriaQuery);
        }
        return null;
    }

    default Long countFiltered(final EntityManager entityManager,
                               final SearchCriteriaForClass searchCriteriaForClass) {
        Class<?> searchedClass = getMetaModelClass(searchCriteriaForClass.getFilteredClass());
        if (searchedClass != null) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<?> root = criteriaQuery.from(extractModelReferenceClass(searchedClass));
            criteriaQuery.select(criteriaBuilder.count(root));
            if (searchCriteriaForClass.getSearchCriteria() != null) {
                List<Predicate> predicates = getPredicates(root, searchCriteriaForClass.getSearchCriteria(), criteriaBuilder);
                criteriaQuery.where(predicates.toArray(Predicate[]::new));
            }
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        }
        return null;
    }

    default List<Predicate> createPredicates(final EntityManager entityManager,
                                             final SearchCriteriaForClass searchCriteriaForClass) {
        Class<?> searchedClass = getMetaModelClass(searchCriteriaForClass.getFilteredClass());
        if (searchedClass != null) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            Root<?> root = initQueryRoot(criteriaBuilder,searchedClass);
            if (searchCriteriaForClass.getSearchCriteria() != null) {
                return getPredicates(root, searchCriteriaForClass.getSearchCriteria(), criteriaBuilder);
            }
        }
        return Collections.emptyList();
    }

    default Root<?> initQueryRoot(final CriteriaBuilder criteriaBuilder, final Class<?> resultClass) {
        return initQueryRoot(criteriaBuilder, resultClass, resultClass);
    }

    default Root<?> initQueryRoot(final CriteriaBuilder criteriaBuilder, final Class<?> resultClass, final Class<?> rootClass) {
        return criteriaBuilder.createQuery(resultClass).from(rootClass);
    }

    default Class<?> extractModelReferenceClass(Class<?> metaModelClass) {
        return metaModelClass.getAnnotation(StaticMetamodel.class).value();
    }

    default Class<?> getMetaModelClass(String targetClassName) {
        final Class<?> cachedClass = cachedMetamodelClasses.get(targetClassName);
        if (cachedClass != null) return cachedClass;
        Class<?> metaModelClass = findMetaClassInPackages(targetClassName);
        cachedMetamodelClasses.put(targetClassName, metaModelClass);
        return metaModelClass;

    }

    default Class<?> findMetaClassInPackages(final String targetClassName) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        final Stack<String> packagesToScan = new Stack<>() {{ push(getModelPackageName()); }};
        Class<?> metaModelClass = null;
        while (!packagesToScan.isEmpty()) {
            final String currentRoot = packagesToScan.pop();
            try {
                metaModelClass = Class.forName(currentRoot + "." + targetClassName + "_");
                break;
            } catch (ClassNotFoundException e1) {
                final File[] files = new File(Objects.requireNonNull(classLoader.getResource(currentRoot.replace(".", "/")))
                        .getPath()).listFiles(File::isDirectory);
                Arrays.stream(Objects.requireNonNull(files)).forEach(f -> packagesToScan.push(currentRoot + "." + f.getName()));
                if (packagesToScan.isEmpty())
                    throw new IllegalArgumentException(String.format("Class %s not exist in package %s", targetClassName, getModelPackageName()));
            }
        }
        return metaModelClass;
    }

    default List<Predicate> getPredicates(final Root<?> root,
                                          final List<SearchCriteria> searchCriterias,
                                          final CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        searchCriterias.forEach(searchCriteria -> {
            Path<?> path = root.get(searchCriteria.getClassFilterField());
            for (Map.Entry<OperationSign, Object> entry : searchCriteria.getCriteriaMap().entrySet()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime dateTime = LocalDateTime
                            .parse(((String) entry.getValue()).substring(0, 10) + " 00:00", formatter);
                    predicates.add(createDateOperatorPredicate(entry.getKey(), (Path<LocalDateTime>) path, dateTime, criteriaBuilder));
                } catch (Exception exception) {
                    if (((SingularAttributePath) path).getAttribute().isId()) {
                        predicates.add(createUuidOperatorPredicate(entry.getKey(), (Path<String>) path, UUID.fromString((String) entry.getValue()), criteriaBuilder));
                    } else if (entry.getValue() instanceof String) {
                        predicates.add(createStringOperatorPredicate(entry.getKey(), (Path<String>) path, (String) entry.getValue(), criteriaBuilder));
                    } else if (entry.getValue() instanceof Number) {
                        predicates.add(createNumericOperatorPredicate(entry.getKey(), (Path<Number>) path, entry.getValue(), criteriaBuilder));
                    } else if (entry.getValue() instanceof SearchCriteriaValuesRange) {
                        SearchCriteriaValuesRange range = (SearchCriteriaValuesRange) entry.getValue();
                        List<Number> rangeList = List.of(range.getStartValue(),range.getEndValue());
                        predicates.add(createNumericOperatorPredicate(entry.getKey(), (Path<Number>) path, rangeList, criteriaBuilder));
                    }
                }
            }
        });

        return predicates;
    }

    default Predicate createUuidOperatorPredicate(final OperationSign operationSign,
                                                  final Path<String> path,
                                                  final UUID compareValue,
                                                  final CriteriaBuilder criteriaBuilder) {
        switch (operationSign) {
            case LIKE:
                return criteriaBuilder.like(path, "%" + compareValue + "%");
            case NOT_EQUAL:
                return criteriaBuilder.notEqual(path, compareValue);
            default: {
                throw new IllegalArgumentException("UUID predicate does not support " + operationSign + " operator");
            }

        }

    }

    default Predicate createStringOperatorPredicate(final OperationSign operationSign,
                                                    final Path<String> path,
                                                    final String compareValue,
                                                    final CriteriaBuilder criteriaBuilder) {
        switch (operationSign) {
            case LIKE:
                return criteriaBuilder.like(path, "%" + compareValue + "%");
            case NOT_EQUAL:
                return criteriaBuilder.notEqual(path, compareValue);
            default:
                throw new IllegalArgumentException("String predicate does not support " + operationSign + " operator");
        }

    }

    default Predicate createNumericOperatorPredicate(final OperationSign operationSign,
                                                     final Path<Number> path,
                                                     final Object compareValue,
                                                     final CriteriaBuilder criteriaBuilder) {
        switch (operationSign) {
            case EQUAL:
                return criteriaBuilder.equal(path, compareValue);
            case NOT_EQUAL:
                return criteriaBuilder.equal(path, compareValue).not();
            case MORE:
                return criteriaBuilder.gt(path, (Number)compareValue);
            case LESS:
                return criteriaBuilder.lt(path, (Number)compareValue);
            default:
                throw new IllegalArgumentException("Number predicate does not support " + operationSign + " operator");
        }

    }

    default Predicate createDateOperatorPredicate(final OperationSign operationSign,
                                                  final Path<LocalDateTime> path,
                                                  final Object compareValue,
                                                  final CriteriaBuilder criteriaBuilder) {
        switch (operationSign) {
            case GREATER_THAN_OR_EQUAL:
                return criteriaBuilder.greaterThanOrEqualTo(path, (LocalDateTime) compareValue);
            case LESS_THAN_OR_EQUAL:
                return criteriaBuilder.lessThanOrEqualTo(path, (LocalDateTime) compareValue);
            default:
                throw new IllegalArgumentException("Date predicate does not support " + operationSign + " operator");
        }

    }

    default void setUpOrderByCondition(final CriteriaBuilder criteriaBuilder,
                                       final CriteriaQuery<?> criteriaQuery,
                                       final From root,
                                       final String sortByField,
                                       final String sortByConnectedObjectField,
                                       final Boolean ascending,
                                       final Class<?> searchedClass) {
        SingularAttribute sortBySingularAttribute;
        if (sortByConnectedObjectField == null) {
            sortBySingularAttribute = getSingularAttributeByClass(getMetaModelClass(searchedClass.getSimpleName()), sortByField);
            if (sortBySingularAttribute != null) {
                doSetUpOrderByCondition(criteriaBuilder, criteriaQuery, ascending, root.get(sortBySingularAttribute));
            }
        } else {
            SingularAttribute rootSingularAttribute = getSingularAttributeByClass(searchedClass, sortByField);
            if (rootSingularAttribute != null) {
                sortBySingularAttribute = getSingularAttributeByClass(getMetaModelClass(rootSingularAttribute.getJavaType().getSimpleName()), sortByConnectedObjectField);
                if (sortBySingularAttribute != null) {
                    doSetUpOrderByCondition(criteriaBuilder, criteriaQuery, ascending, root.get(rootSingularAttribute).get(sortBySingularAttribute));
                }
            }
        }
    }

    default void doSetUpOrderByCondition(final CriteriaBuilder criteriaBuilder,
                                         final CriteriaQuery criteriaQuery,
                                         final Boolean ascending,
                                         final Path sortByPath) {
        if (ascending) {
            criteriaQuery.orderBy(criteriaBuilder.asc(sortByPath));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(sortByPath));
        }
    }

    default SingularAttribute getSingularAttributeByClass(final Class<?> metaModelClass, String sortByField) {
        if (metaModelClass != null) {
            for (Field field : metaModelClass.getDeclaredFields()) {
                if (field.getName().equals(sortByField)) {
                    try {
                        field.setAccessible(true);
                        return (SingularAttribute) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(String.format("Attempt to access to field %s in class %s failed", metaModelClass.getName(), sortByField));
                    }
                }
            }
        }
        return null;
    }
}