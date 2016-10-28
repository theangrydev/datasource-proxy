package net.ttddyy.dsproxy.proxy;

import org.junit.Test;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ResultSetProxyLogicTest {

    private static final int NUMBER_OF_COLUMNS = 3;
    private static final String COLUMN_1_LABEL = "FIRST";
    private static final String COLUMN_2_LABEL = "SECOND";
    private static final String COLUMN_3_LABEL = "THIRD";

    @Test
    public void getTargetReturnsTheResultSetFromTheTarget() throws Throwable {
        ResultSet resultSet = exampleResultSet();
        ResultSetProxyLogic resultSetProxyLogic = ResultSetProxyLogic.resultSetProxyLogic(resultSet);

        Method getTarget = ProxyJdbcObject.class.getMethod("getTarget");

        Object result = resultSetProxyLogic.invoke(getTarget, null);

        assertThat(result).isSameAs(resultSet);
    }

    @Test
    public void getResultSetMetaDataReturnsTheResultSetMetaDataFromTheTarget() throws Throwable {
        ResultSet resultSet = exampleResultSet();
        ResultSetProxyLogic resultSetProxyLogic = ResultSetProxyLogic.resultSetProxyLogic(resultSet);

        Method getMetaData = ResultSet.class.getMethod("getMetaData");

        Object result = resultSetProxyLogic.invoke(getMetaData, null);

        assertThat(result).isSameAs(resultSet.getMetaData());
    }

    @Test
    public void closeCallsCloseOnTheTarget() throws Throwable {
        ResultSet resultSet = exampleResultSet();
        ResultSetProxyLogic resultSetProxyLogic = ResultSetProxyLogic.resultSetProxyLogic(resultSet);

        Method close = ResultSet.class.getMethod("close");

        resultSetProxyLogic.invoke(close, null);

        verify(resultSet).close();
    }

    //TODO: more tests
    @Test
    public void getColumnOnClosedResultSetThatHasBeenConsumedOnceThrowsException() throws Throwable {
        fail("TODO");
    }

    private ResultSet exampleResultSet() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData resultSetMetaData = exampleResultSetMetaData();
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        return resultSet;
    }

    private ResultSetMetaData exampleResultSetMetaData() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(NUMBER_OF_COLUMNS);
        when(metaData.getColumnLabel(1)).thenReturn(COLUMN_1_LABEL);
        when(metaData.getColumnLabel(2)).thenReturn(COLUMN_2_LABEL);
        when(metaData.getColumnLabel(3)).thenReturn(COLUMN_3_LABEL);
        return metaData;
    }
}