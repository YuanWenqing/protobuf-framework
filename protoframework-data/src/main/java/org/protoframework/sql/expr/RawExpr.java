package org.protoframework.sql.expr;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.protoframework.sql.ISqlOperation;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class RawExpr extends AbstractExpression {

  private final String sql;
  private final List<Object> values;

  public RawExpr(@Nonnull String sql) {
    this(sql, Collections.emptyList());
  }

  public RawExpr(@Nonnull String sql, @Nonnull Collection<?> values) {
    this.sql = sql;
    this.values = ImmutableList.copyOf(values);
  }

  public String getSql() {
    return sql;
  }

  public List<Object> getValues() {
    return values;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    return sb.append(sql);
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    Iterator<Object> iter = values.iterator();
    for (String part : StringUtils.splitPreserveAllTokens(sql, "?")) {
      sb.append(part);
      if (iter.hasNext()) {
        sb.append(iter.next());
      } else {
        sb.append("?");
      }
    }
    return sb;
  }

  @Override
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    collectedValues.addAll(values);
    return collectedValues;
  }

  @Override
  public int comparePrecedence(@Nonnull ISqlOperation outerOp) {
    return 1;
  }

}
