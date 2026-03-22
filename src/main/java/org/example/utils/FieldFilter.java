package org.example.utils;
import org.example.model.FieldChange;

import java.util.*;

/**
 * 字段过滤器 - 负责 include/exclude 规则
 */
class FieldFilter {

    public static List<FieldChange> filterByExcludes(List<FieldChange> changes, Set<String> excludeFields) {
        if (changes == null || changes.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> excludeSet = excludeFields != null
                ? new HashSet<>(excludeFields)
                : Collections.emptySet();

        List<FieldChange> result = new ArrayList<>();
        for (FieldChange change : changes) {
            if (!excludeSet.contains(change.getFieldName())) {
                result.add(change);
            }
        }
        return result;
    }
    public static List<FieldChange> filterByIncludes(List<FieldChange> changes, Set<String> includeFields) {
        if (changes == null || changes.isEmpty()) {
            return new ArrayList<>();
        }

        if (includeFields == null || includeFields.isEmpty()) {
            return new ArrayList<>();
        }

        List<FieldChange> result = new ArrayList<>();
        for (FieldChange change : changes) {
            if (includeFields.contains(change.getFieldName())) {
                result.add(change);
            }
        }
        return result;
    }

    public static boolean isMeaningfulChange(FieldChange change) {
        return !Objects.equals(change.getOldValue(), change.getNewValue());
    }
}