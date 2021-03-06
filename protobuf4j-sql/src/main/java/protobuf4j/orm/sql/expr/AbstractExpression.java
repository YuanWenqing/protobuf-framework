package protobuf4j.orm.sql.expr;

import protobuf4j.orm.sql.AbstractSqlObject;
import protobuf4j.orm.sql.IExpression;

/**
 * 表达式基类，实现一些同一的方法
 *
 * author: yuanwq
 * date: 2018/7/16
 */
public abstract class AbstractExpression extends AbstractSqlObject implements IExpression {
  @Override
  public IExpression and(IExpression right) {
    return Expressions.and(this, right);
  }

  @Override
  public IExpression or(IExpression right) {
    return Expressions.or(this, right);
  }

  @Override
  public IExpression xor(IExpression right) {
    return Expressions.xor(this, right);
  }

  @Override
  public IExpression not() {
    return Expressions.not(this);
  }
}
