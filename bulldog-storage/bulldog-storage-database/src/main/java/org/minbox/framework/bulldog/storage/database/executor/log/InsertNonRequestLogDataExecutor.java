package org.minbox.framework.bulldog.storage.database.executor.log;

import org.minbox.framework.bulldog.common.utils.Assert;
import org.minbox.framework.bulldog.storage.database.executor.InsertDataExecutor;
import org.minbox.framework.bulldog.storage.database.executor.mapping.parameter.LongParameterTypeMapping;
import org.minbox.framework.bulldog.storage.database.executor.mapping.parameter.ParameterTypeMapping;
import org.minbox.framework.bulldog.storage.database.executor.mapping.parameter.StringParameterTypeMapping;
import org.minbox.framework.bulldog.storage.database.executor.variable.ParameterVariable;
import org.minbox.framework.bulldog.storage.database.table.NonRequestLogTable;
import org.minbox.framework.fulldog.core.pojo.NonRequestLogDetails;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.minbox.framework.bulldog.storage.database.executor.variable.VariableKeys.NON_REQUEST_LOG_ID;
import static org.minbox.framework.bulldog.storage.database.executor.variable.VariableKeys.NON_REQUEST_LOG_INSTANCE;

/**
 * Insert {@link NonRequestLogDetails} executor
 *
 * @author 恒宇少年
 */
public class InsertNonRequestLogDataExecutor extends InsertDataExecutor<String> {

    @Override
    protected String getSql() {
        return NonRequestLogTable.SQL.INSERT.single();
    }

    @Override
    protected List<ParameterTypeMapping> getParameterMappings(ParameterVariable variable) {
        NonRequestLogDetails details = variable.getVariable(NON_REQUEST_LOG_INSTANCE);
        return Arrays.asList(
                new StringParameterTypeMapping(1, variable.getVariable(NON_REQUEST_LOG_ID)),
                new StringParameterTypeMapping(2, details.getTraceId()),
                new StringParameterTypeMapping(3, details.getSpanId()),
                new StringParameterTypeMapping(4, details.getParentSpanId()),
                new StringParameterTypeMapping(5, details.getServiceId()),
                new LongParameterTypeMapping(6, details.getStartTime()),
                new LongParameterTypeMapping(7, details.getEndTime()),
                new LongParameterTypeMapping(8, details.getTimeConsuming()),
                this.notEmptyToParseJson(9, details.getMetadata()),
                new StringParameterTypeMapping(10, details.getGroup()),
                new StringParameterTypeMapping(11, details.getTag())
        );
    }

    @Override
    protected void preExecute(ParameterVariable variable) {
        String logId = UUID.randomUUID().toString();
        NonRequestLogDetails details = variable.getVariable(NON_REQUEST_LOG_INSTANCE);
        Assert.notNull(details, "The NonRequestLogDetails cannot be null.");
        Assert.notEmpty(details.getTraceId(), "The traceId cannot be empty.");
        Assert.notEmpty(details.getSpanId(), "The spanId cannot be empty.");
        Assert.notEmpty(details.getServiceId(), "The serviceId cannot be empty.");
        variable.putVariable(NON_REQUEST_LOG_ID, logId);
    }

    @Override
    protected String afterExecute(ParameterVariable variable) {
        return variable.getVariable(NON_REQUEST_LOG_ID);
    }
}
