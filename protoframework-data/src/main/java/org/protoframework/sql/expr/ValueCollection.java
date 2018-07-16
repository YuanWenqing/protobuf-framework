package org.protoframework.sql.expr;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.protoframework.sql.ISqlOperation;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * 值集合
 *
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class ValueCollection extends AbstractExpression {
  private final List<Object> values;
  private String field;

  public ValueCollection(@Nonnull Collection<?> values) {
    this.values = ImmutableList.copyOf(values);
  }

  /**
   * @param field 与{@code value}关联的字段，便于确定{@code value}转换SqlValue时的类型
   */
  public ValueCollection(List<Object> values, String field) {
    this.values = values;
    this.field = field;
  }

  public List<Object> getValues() {
    return values;
  }

  public boolean isEmpty() {
    return values.isEmpty();
  }

  /**
   * @see #setField(String)
   */
  public String getField() {
    return field;
  }

  /**
   * @param field 与{@code value}关联的字段，便于确定{@code value}转换SqlValue时的类型
   */
  public ValueCollection setField(String field) {
    this.field = field;
    return this;
  }

  @Override
  public int comparePrecedence(@Nonnull ISqlOperation outerOp) {
    return 1;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    sb.append(String.format("(%s)", StringUtils.repeat("?", ",", values.size())));
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    sb.append(String.format("(%s)", StringUtils.join(values, ",")));
    return sb;
  }

  @Override
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    for (Object value : values) {
      collectedValues.add(new Value(value, field));
    }
    return collectedValues;
  }

}
