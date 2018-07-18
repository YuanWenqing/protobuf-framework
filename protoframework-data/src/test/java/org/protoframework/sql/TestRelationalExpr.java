package org.protoframework.sql;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.protoframework.sql.expr.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/17
 */
public class TestRelationalExpr {
  @Test
  public void testFieldValue() {
    RelationalExpr expr;
    List<ISqlValue> sqlValues;

    {
      // =
      expr = FieldValues.eq("a", 1);
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertEquals("a", ((Column) expr.getLeft()).getColumn());
      assertTrue(expr.getRight() instanceof Value);
      assertEquals(RelationalOp.EQ, expr.getOp());
      assertEquals("a=?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a=1", expr.toSolidSql(new StringBuilder()).toString());
      sqlValues = expr.collectSqlValue(Lists.newArrayList());
      assertEquals(1, sqlValues.size());
      assertEquals("a", sqlValues.get(0).getField());
      assertEquals(1, sqlValues.get(0).getValue());
    }
    {
      // !=
      expr = FieldValues.ne("a", 1);
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Value);
      assertEquals(RelationalOp.NE, expr.getOp());
      assertEquals("a!=?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a!=1", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // <
      expr = FieldValues.lt("a", 1);
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Value);
      assertEquals(RelationalOp.LT, expr.getOp());
      assertEquals("a<?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a<1", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // <=
      expr = FieldValues.lte("a", 1);
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Value);
      assertEquals(RelationalOp.LTE, expr.getOp());
      assertEquals("a<=?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a<=1", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // >
      expr = FieldValues.gt("a", 1);
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Value);
      assertEquals(RelationalOp.GT, expr.getOp());
      assertEquals("a>?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a>1", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // >=
      expr = FieldValues.gte("a", 1);
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Value);
      assertEquals(RelationalOp.GTE, expr.getOp());
      assertEquals("a>=?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a>=1", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // is null
      expr = FieldValues.isNull("a");
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertNull(expr.getRight());
      assertEquals(RelationalOp.IS_NULL, expr.getOp());
      assertEquals("a IS NULL", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a IS NULL", expr.toSolidSql(new StringBuilder()).toString());
      sqlValues = expr.collectSqlValue(Lists.newArrayList());
      assertEquals(0, sqlValues.size());
    }
    {
      // is not null
      expr = FieldValues.isNotNull("a");
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertNull(expr.getRight());
      assertEquals(RelationalOp.IS_NOT_NULL, expr.getOp());
      assertEquals("a IS NOT NULL", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a IS NOT NULL", expr.toSolidSql(new StringBuilder()).toString());
      sqlValues = expr.collectSqlValue(Lists.newArrayList());
      assertEquals(0, sqlValues.size());
    }
    {
      // like
      expr = FieldValues.like("a", "pattern");
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Value);
      assertEquals(RelationalOp.LIKE, expr.getOp());
      assertEquals("a LIKE ?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a LIKE 'pattern'", expr.toSolidSql(new StringBuilder()).toString());
      sqlValues = expr.collectSqlValue(Lists.newArrayList());
      assertEquals(1, sqlValues.size());
      assertEquals("a", sqlValues.get(0).getField());
      assertEquals("pattern", sqlValues.get(0).getValue());
    }
    {
      // between
      expr = FieldValues.between("a", 1, 2);
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof BetweenExpr);
      assertEquals(RelationalOp.BETWEEN, expr.getOp());
      assertEquals("a BETWEEN ? AND ?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a BETWEEN 1 AND 2", expr.toSolidSql(new StringBuilder()).toString());
      sqlValues = expr.collectSqlValue(Lists.newArrayList());
      assertEquals(2, sqlValues.size());
      assertEquals("a", sqlValues.get(0).getField());
      assertEquals(1, sqlValues.get(0).getValue());
      assertEquals("a", sqlValues.get(1).getField());
      assertEquals(2, sqlValues.get(1).getValue());
      BetweenExpr betweenExpr = (BetweenExpr) expr.getRight();
      assertTrue(betweenExpr.getMin() instanceof Value);
      assertTrue(betweenExpr.getMax() instanceof Value);

      expr = RelationalExpr
          .between(Column.of("a"), FieldValues.add("b", 1), FieldValues.mod("c", 2));
      System.out.println(expr);
      assertEquals("a BETWEEN (b+?) AND (c MOD ?)",
          expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a BETWEEN (b+1) AND (c MOD 2)",
          expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // in
      expr = FieldValues.in("a", Lists.newArrayList(2, 3));
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof ValueCollection);
      assertEquals(RelationalOp.IN, expr.getOp());
      assertEquals("a IN (?,?)", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a IN (2,3)", expr.toSolidSql(new StringBuilder()).toString());
      sqlValues = expr.collectSqlValue(Lists.newArrayList());
      assertEquals(2, sqlValues.size());
      assertEquals("a", sqlValues.get(0).getField());
      assertEquals(2, sqlValues.get(0).getValue());
      assertEquals("a", sqlValues.get(1).getField());
      assertEquals(3, sqlValues.get(1).getValue());
    }
    {
      // nin
      expr = FieldValues.nin("a", Lists.newArrayList(2, 3));
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof ValueCollection);
      assertEquals(RelationalOp.NIN, expr.getOp());
      assertEquals("a NOT IN (?,?)", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a NOT IN (2,3)", expr.toSolidSql(new StringBuilder()).toString());
      sqlValues = expr.collectSqlValue(Lists.newArrayList());
      assertEquals(2, sqlValues.size());
      assertEquals("a", sqlValues.get(0).getField());
      assertEquals(2, sqlValues.get(0).getValue());
      assertEquals("a", sqlValues.get(1).getField());
      assertEquals(3, sqlValues.get(1).getValue());
    }
  }

  @Test
  public void testFieldField() {
    RelationalExpr expr;
    List<ISqlValue> sqlValues;

    {
      // =
      expr = FieldFields.eq("a", "b");
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Column);
      assertEquals(RelationalOp.EQ, expr.getOp());
      assertEquals("a=b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a=b", expr.toSolidSql(new StringBuilder()).toString());
      sqlValues = expr.collectSqlValue(Lists.newArrayList());
      assertEquals(0, sqlValues.size());
    }
    {
      // !=
      expr = FieldFields.ne("a", "b");
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Column);
      assertEquals(RelationalOp.NE, expr.getOp());
      assertEquals("a!=b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a!=b", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // <
      expr = FieldFields.lt("a", "b");
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Column);
      assertEquals(RelationalOp.LT, expr.getOp());
      assertEquals("a<b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a<b", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // <=
      expr = FieldFields.lte("a", "b");
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Column);
      assertEquals(RelationalOp.LTE, expr.getOp());
      assertEquals("a<=b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a<=b", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // >
      expr = FieldFields.gt("a", "b");
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Column);
      assertEquals(RelationalOp.GT, expr.getOp());
      assertEquals("a>b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a>b", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // >=
      expr = FieldFields.gte("a", "b");
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Column);
      assertEquals(RelationalOp.GTE, expr.getOp());
      assertEquals("a>=b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a>=b", expr.toSolidSql(new StringBuilder()).toString());
    }
  }

  @Test
  public void testEmbedding() {
    RelationalExpr expr = RelationalExpr.eq(FieldValues.eq("a", 1), FieldFields.gt("a", "b"));
    System.out.println(expr);
    assertEquals("(a=?)=(a>b)", expr.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("(a=1)=(a>b)", expr.toSolidSql(new StringBuilder()).toString());
    List<ISqlValue> sqlValues = expr.collectSqlValue(Lists.newArrayList());
    assertEquals(1, sqlValues.size());
    assertEquals("a", sqlValues.get(0).getField());
    assertEquals(1, sqlValues.get(0).getValue());

    expr = RelationalExpr.gt(FieldFields.add("a", "b"), FieldFields.multiply("c", "d"));
    System.out.println(expr);
    assertEquals("(a+b)>(c*d)", expr.toSqlTemplate(new StringBuilder()).toString());

    expr = RelationalExpr.lt(FieldFields.eq("a", "b"), FieldFields.and("c", "d"));
    System.out.println(expr);
    assertEquals("(a=b)<(c AND d)", expr.toSqlTemplate(new StringBuilder()).toString());

  }
}
