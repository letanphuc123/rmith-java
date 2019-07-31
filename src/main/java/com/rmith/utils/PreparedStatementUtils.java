package com.rmith.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.List;

/**
 *
 * @author Le Tan Phuc
 
 */
public class PreparedStatementUtils {

    public PreparedStatement bindParameter(PreparedStatement rs, List<String> listValue, Integer indexBinding) {
        try {
            for (String value : listValue) {
                rs.setString(indexBinding++, value);
            }
        } catch (SQLException e) {
        }
        return rs;
    }

    public PreparedStatement bindParameter(PreparedStatement rs, int[] arrValue, Integer indexBinding) {
        try {
            for (int value : arrValue) {
                rs.setInt(indexBinding++, value);
            }
        } catch (SQLException e) {
        }
        return rs;
    }

    public String createInListPattern(int n) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            result.append("?,");

        }
        return result.substring(0, result.length() - 1);
    }

     public boolean checkConstraint(PreparedStatement ps, String contraintKey) throws SQLException {
        SQLWarning warning = ps.getWarnings();
        while (warning != null) {
            if (warning.getErrorCode() == 1451 && warning.getMessage().contains(contraintKey)) {
                return true;
            }
            warning = warning.getNextWarning();
        }
        return false;
    }
}
