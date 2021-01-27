package com.kehua.xml.control.table.tablecell;

/**
 * HuangTengfei
 * 2020/12/24
 * description：
 */
public class EditingNumberTableCell<T, E extends Number> extends EditingTextTableCell<T, E> {

    protected E max;
    protected E min;

    public EditingNumberTableCell(Class<E> type) {
        this(type, null, null, null, null);
    }

    public EditingNumberTableCell(Class<E> type, Integer limit) {
        this(type, null, limit, null, null);
    }

    public EditingNumberTableCell(Class<E> type, String tipText) {
        this(type, tipText, null, null);
    }

    public EditingNumberTableCell(Class<E> type, String tipText, Integer limit) {
        this(type, tipText, limit, null, null);
    }

    public EditingNumberTableCell(Class<E> type, E min, E max) {
        this(type, null, null, min, max);
    }

    public EditingNumberTableCell(Class<E> type, Integer limit, E min, E max) {
        this(type, null, limit, min, max);
    }

    public EditingNumberTableCell(Class<E> type, String tipText, E min, E max) {
        this(type, tipText, null, min, max);
    }

    public EditingNumberTableCell(Class<E> type, String tipText, Integer limit, E min, E max) {
        super(type, tipText, limit);
        this.min = min;
        this.max = max;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected boolean check(Change<E> change) {
        E value = change.newValue;
        if (max != null && value != null && ((Comparable) max).compareTo(value) < 0) {
            change.newValue = max;
            return false;
        }
        if (min != null && value != null && ((Comparable) min).compareTo(value) > 0) {
            change.newValue = min;
            return false;
        }
        return true;
    }

}
