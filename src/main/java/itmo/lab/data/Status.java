package itmo.lab.data;

/**
 * Статус работника.
 *
 * Возможные значения: уволен, принят, рекомендован к повышению,
 * обычный, на испытательном сроке.
 */
public enum Status {
    FIRED,
    HIRED,
    RECOMMENDED_FOR_PROMOTION,
    REGULAR,
    PROBATION
}