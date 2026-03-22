package org.example.utils;

import org.example.enums.OperationType;
import org.example.model.ChangeResult;
import org.example.model.FailedField;
import org.example.model.FieldChange;

import java.util.*;


public class ChangeResultFormatter {

    public static String format(ChangeResult changeResult, OperationType operationType) {
        if (changeResult == null) return "";
        if (!changeResult.hasChanges()) return "";

        List<FieldChange> changes = changeResult.getChanges();

        changes = changes.stream()
                .filter(FieldFilter::isMeaningfulChange)
                .toList();

        if (changes.isEmpty()) {
            return "";
        }

        return ValueFormatter.truncate(ChangeStringBuilder.build(changes, operationType));
    }

    public static String formatWithExcludes(ChangeResult changeResult,
                                            OperationType operationType,
                                            Set<String> excludeFields) {
        if (changeResult == null) return "";
        if (!changeResult.hasChanges()) return "";

        List<FieldChange> filteredChanges = FieldFilter.filterByExcludes(
                changeResult.getChanges(), excludeFields);

        filteredChanges = filteredChanges.stream()
                .filter(FieldFilter::isMeaningfulChange)
                .toList();

        if (filteredChanges.isEmpty()) {
            return "";
        }

        return ValueFormatter.truncate(ChangeStringBuilder.build(filteredChanges, operationType));
    }

    public static String formatWithIncludes(ChangeResult changeResult,
                                            OperationType operationType,
                                            Set<String> includeFields) {
        if (changeResult == null) return "";
        if (!changeResult.hasChanges()) return "";

        List<FieldChange> filteredChanges = FieldFilter.filterByIncludes(
                changeResult.getChanges(), includeFields);

        filteredChanges = filteredChanges.stream()
                .filter(FieldFilter::isMeaningfulChange)
                .toList();

        if (filteredChanges.isEmpty()) {
            return "";
        }

        return ValueFormatter.truncate(ChangeStringBuilder.build(filteredChanges, operationType));
    }
}
