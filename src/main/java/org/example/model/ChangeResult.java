package org.example.model;
import java.util.ArrayList;
import java.util.List;


/**
 * 字段变更结果
 */

public class ChangeResult {

    private List<FieldChange> changes;
    private List<FailedField> failedFields;

    public ChangeResult() {
        this.changes = new ArrayList<>();
        this.failedFields = new ArrayList<>();
    }

    public void addChange(FieldChange change) {
        this.changes.add(change);
    }

    public void addFailedField(FailedField failedField) {
        this.failedFields.add(failedField);
    }

    public void addFailedField(String fieldName, Exception e) {
        this.failedFields.add(new FailedField(fieldName, e));
    }

    public boolean hasChanges() {
        return !changes.isEmpty();
    }

    public boolean hasFailedFields() {
        return !failedFields.isEmpty();
    }

    public List<FieldChange> getChanges() { return changes; }
    public void setChanges(List<FieldChange> changes) { this.changes = changes; }
    public List<FailedField> getFailedFields() { return failedFields; }
    public void setFailedFields(List<FailedField> failedFields) { this.failedFields = failedFields; }
}