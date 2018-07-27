package org.protoframework.sql.expr;

import org.protoframework.sql.ISqlOperator;

/**
 * @author: yuanwq
 * @date: 2018/7/11
 */
public enum ArithmeticOp implements ISqlOperator {
  ADD("+"),

  SUBTRACT("-"),

  MULTIPLY("*"),

  DIVIDE("/"),

  DIV_ROUND(" DIV "),

  MOD(" MOD ");

  private final String op;

  ArithmeticOp(String op) {
    this.op = op;
  }

  @Override
  public String getOp() {
    return op;
  }

}
