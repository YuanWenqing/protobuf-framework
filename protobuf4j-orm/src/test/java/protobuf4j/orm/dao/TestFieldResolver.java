package protobuf4j.orm.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.MapEntry;
import org.junit.Test;
import protobuf4j.core.ProtoMessageHelper;
import protobuf4j.orm.converter.FieldConversionException;
import protobuf4j.orm.converter.FieldResolver;
import protobuf4j.test.proto.TestModel;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/23
 */
public class TestFieldResolver {
  private ProtoMessageHelper<TestModel.MsgA> helperA =
      ProtoMessageHelper.getHelper(TestModel.MsgA.class);
  FieldResolver<TestModel.MsgA> fieldResolver = new FieldResolver<>(TestModel.MsgA.class);
  private ProtoMessageHelper<TestModel.MsgB> helperB =
      ProtoMessageHelper.getHelper(TestModel.MsgB.class);

  @Test
  public void testSingularFieldResolveSqlValueType() {
    assertEquals(Integer.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("int32")));
    assertEquals(Long.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("int64")));
    assertEquals(Float.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("float")));
    assertEquals(Double.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("double")));
    assertEquals(Integer.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("bool")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("string")));
    assertEquals(Integer.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("enuma")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("bytes")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("msgb")));
  }

  @Test
  public void testRepeatedFieldResolveSqlValueType() {
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("int32_arr")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("int64_arr")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("float_arr")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("double_arr")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("bool_arr")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("string_arr")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("enuma_arr")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("bytes_arr")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("msgb_arr")));
  }

  @Test
  public void testMapFieldResolveSqlValueType() {
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("int32_map")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("int64_map")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("float_map")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("double_map")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("bool_map")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("string_map")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("enuma_map")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("bytes_map")));
    assertEquals(String.class,
        fieldResolver.resolveSqlValueType(helperA.getFieldDescriptor("msgb_map")));
  }

  @Test
  public void testSingularFieldToSqlValue() {
    assertEquals(1, fieldResolver.toSqlValue(helperA.getFieldDescriptor("int32"), 1));
    assertEquals(1L, fieldResolver.toSqlValue(helperA.getFieldDescriptor("int64"), 1));
    assertEquals(1.1f, fieldResolver.toSqlValue(helperA.getFieldDescriptor("float"), 1.1));
    assertEquals(1.1, fieldResolver.toSqlValue(helperA.getFieldDescriptor("double"), 1.1));
    assertEquals(1, fieldResolver.toSqlValue(helperA.getFieldDescriptor("bool"), true));
    assertEquals(0, fieldResolver.toSqlValue(helperA.getFieldDescriptor("bool"), false));
    assertEquals("1", fieldResolver.toSqlValue(helperA.getFieldDescriptor("string"), "1"));
    assertEquals("str", fieldResolver.toSqlValue(helperA.getFieldDescriptor("string"), "str"));
    assertEquals(0,
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("enuma"), TestModel.EnumA.EA0));
    assertEquals(2,
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("enuma"), TestModel.EnumA.EA2));
    assertEquals(4,
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("enuma"), TestModel.EnumA.EA4));

    // 兼容
    assertEquals(1L, fieldResolver.toSqlValue(helperA.getFieldDescriptor("int64"), 1));
    assertEquals(1f, fieldResolver.toSqlValue(helperA.getFieldDescriptor("float"), 1));
    assertEquals(1, fieldResolver.toSqlValue(helperA.getFieldDescriptor("bool"), 1));
    assertEquals(2, fieldResolver.toSqlValue(helperA.getFieldDescriptor("enuma"), 2));
  }

  @Test
  public void testMessageField() {
    TestModel.MsgA msgA =
        TestModel.MsgA.newBuilder().setInt32(1).setInt64(1).setFloat(1).setDouble(1).setBool(true)
            .setString("1").setBytes(ByteString.copyFromUtf8("a")).setEnuma(TestModel.EnumA.EA2)
            .setMsgb(TestModel.MsgB.newBuilder().setId("").build()).addInt32Arr(1).addInt64Arr(1)
            .addFloatArr(1).addDoubleArr(1).addBoolArr(false).addStringArr("a")
            .addBytesArr(ByteString.EMPTY).addEnumaArr(TestModel.EnumA.EA2)
            .addMsgbArr(TestModel.MsgB.getDefaultInstance()).putInt32Map("", 1).putInt64Map("", 1)
            .putFloatMap("", 1).putDoubleMap("", 1).putBoolMap(1, false).putStringMap("", "a")
            .putBytesMap("", ByteString.EMPTY).putEnumaMap("", TestModel.EnumA.EA4)
            .putMsgbMap("", TestModel.MsgB.getDefaultInstance()).build();
    System.out.println();
    Object sqlValue = fieldResolver.toSqlValue(helperA.getFieldDescriptor("msga"), msgA);
    String expectValue =
        "{\"int32\":1,\"int64\":1,\"float\":1.0,\"double\":1.0,\"bool\":true,\"string\":\"1\",\"bytes\":\"YQ==\",\"enuma\":2,\"msgb\":{\"id\":\"\",\"create_time\":0},\"msga\":null,\"int32_arr\":[1],\"int64_arr\":[1],\"float_arr\":[1.0],\"double_arr\":[1.0],\"bool_arr\":[false],\"string_arr\":[\"a\"],\"bytes_arr\":[\"\"],\"enuma_arr\":[2],\"msgb_arr\":[{\"id\":\"\",\"create_time\":0}],\"int32_map\":{\"\":1},\"int64_map\":{\"\":1},\"float_map\":{\"\":1.0},\"double_map\":{\"\":1.0},\"bool_map\":{\"1\":false},\"string_map\":{\"\":\"a\"},\"bytes_map\":{\"\":\"\"},\"enuma_map\":{\"\":4},\"msgb_map\":{\"\":{\"id\":\"\",\"create_time\":0}}}";
    assertEquals(expectValue, sqlValue);
    assertEquals(msgA, fieldResolver.fromSqlValue(helperA.getFieldDescriptor("msga"), expectValue));
    assertEquals(msgA, fieldResolver.fromSqlValue(helperA.getFieldDescriptor("msga"), msgA));
    assertEquals(TestModel.MsgA.getDefaultInstance(),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("msga"), null));
    assertEquals(TestModel.MsgA.getDefaultInstance(),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("msga"), ""));
  }

  @Test
  public void testToSqlValueTypeMismatch() {
    try {
      fieldResolver.toSqlValue(helperA.getFieldDescriptor("int32"), 1.1);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
    try {
      fieldResolver.toSqlValue(helperA.getFieldDescriptor("int64"), "");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
    try {
      fieldResolver.toSqlValue(helperA.getFieldDescriptor("float"), "");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
    try {
      fieldResolver.toSqlValue(helperA.getFieldDescriptor("double"), "");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
    try {
      fieldResolver.toSqlValue(helperA.getFieldDescriptor("bool"), "");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
    try {
      fieldResolver.toSqlValue(helperA.getFieldDescriptor("enuma"), "");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testRepeatedFieldToSqlValue() {
    assertEquals("",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("int32_arr"), Lists.newArrayList()));
    assertEquals("",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("int32_arr"), Sets.newHashSet()));
    assertEquals("1,2,", fieldResolver
        .toSqlValue(helperA.getFieldDescriptor("int32_arr"), Lists.newArrayList(1, 2)));
    assertEquals("1,2,", fieldResolver
        .toSqlValue(helperA.getFieldDescriptor("int64_arr"), Lists.newArrayList(1L, 2L)));
    assertEquals("1.1,2.2,", fieldResolver
        .toSqlValue(helperA.getFieldDescriptor("float_arr"), Lists.newArrayList(1.1f, 2.2f)));
    assertEquals("1.1,2.2,", fieldResolver
        .toSqlValue(helperA.getFieldDescriptor("double_arr"), Lists.newArrayList(1.1, 2.2)));
    assertEquals("1,0,", fieldResolver
        .toSqlValue(helperA.getFieldDescriptor("bool_arr"), Lists.newArrayList(true, false)));
    assertEquals(",a,", fieldResolver
        .toSqlValue(helperA.getFieldDescriptor("string_arr"), Lists.newArrayList("", "a")));
    assertEquals(",",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("string_arr"), Lists.newArrayList("")));
    assertEquals("%2c,", fieldResolver
        .toSqlValue(helperA.getFieldDescriptor("string_arr"), Lists.newArrayList(",")));
    assertEquals("%2c%25,", fieldResolver
        .toSqlValue(helperA.getFieldDescriptor("string_arr"), Lists.newArrayList(",%")));
    assertEquals("2,0,", fieldResolver.toSqlValue(helperA.getFieldDescriptor("enuma_arr"),
        Lists.newArrayList(TestModel.EnumA.EA2, TestModel.EnumA.EA0)));

    // 兼容
    assertEquals("1,2,", fieldResolver
        .toSqlValue(helperA.getFieldDescriptor("int64_arr"), Lists.newArrayList(1, 2)));
    assertEquals("1.0,2.0,", fieldResolver
        .toSqlValue(helperA.getFieldDescriptor("double_arr"), Lists.newArrayList(1, 2)));
    assertEquals("0,1,",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("bool_arr"), Lists.newArrayList(0, 1)));
    assertEquals("2,",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("enuma_arr"), Lists.newArrayList(2)));
    assertEquals("",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("bytes_arr"), Lists.newArrayList()));

    try {
      fieldResolver.toSqlValue(helperA.getFieldDescriptor("int32_arr"), 1);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
    try {
      fieldResolver.toSqlValue(helperA.getFieldDescriptor("msgb_arr"), Lists.newArrayList());
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @SuppressWarnings({"rawtype", "unchecked"})
  private Map map(Object... kv) {
    Map map = new LinkedHashMap();
    if (kv == null || kv.length == 0) {
      return map;
    }
    for (int i = 0; i < kv.length; i += 2) {
      map.put(kv[i], kv[i + 1]);
    }
    return map;
  }

  @Test
  public void testMapFieldToSqlValue() {
    // map
    assertEquals("{}", fieldResolver.toSqlValue(helperA.getFieldDescriptor("int32_map"), map()));
    assertEquals("{\"a\":1,\"b\":2}",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("int32_map"), map("a", 1, "b", 2)));
    assertEquals("{\"1\":1}",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("bool_map"), map(1, true)));
    assertEquals("{\"\":\"a\"}",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("string_map"), map("", "a")));
    assertEquals("{\":\":\";\"}",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("string_map"), map(":", ";")));
    assertEquals("{\":\":\"%\"}",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("string_map"), map(":", "%")));
    assertEquals("{\"a\":0}", fieldResolver
        .toSqlValue(helperA.getFieldDescriptor("enuma_map"), map("a", TestModel.EnumA.EA0)));

    // list
    TestModel.MsgA msga =
        TestModel.MsgA.newBuilder().putInt64Map("a", 1).putInt64Map("b", 2).build();
    assertEquals("{\"a\":1,\"b\":2}", fieldResolver
        .toSqlValue(helperA.getFieldDescriptor("int64_map"),
            helperA.getFieldValue(msga, "int64_map")));

    // 兼容
    assertEquals("{\"\":2}",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("int64_map"), map("", 2)));
    assertEquals("{\"a\":2.0}",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("double_map"), map("a", 2)));
    assertEquals("{\"0\":1}",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("bool_map"), map(0, 1)));
    assertEquals("{\"a\":2}",
        fieldResolver.toSqlValue(helperA.getFieldDescriptor("enuma_map"), map("a", 2)));

    assertEquals("{}", fieldResolver.toSqlValue(helperA.getFieldDescriptor("bytes_map"), map()));

    try {
      fieldResolver.toSqlValue(helperA.getFieldDescriptor("int32_map"), 1);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
    try {
      fieldResolver.toSqlValue(helperA.getFieldDescriptor("msgb_map"), map());
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  @SuppressWarnings("rawtype")
  public void testSingularFieldFromSqlValue() {
    assertEquals(1, fieldResolver.fromSqlValue(helperA.getFieldDescriptor("int32"), 1));
    assertEquals(1L, fieldResolver.fromSqlValue(helperA.getFieldDescriptor("int64"), 1));
    assertEquals(1L, fieldResolver.fromSqlValue(helperA.getFieldDescriptor("int64"), 1L));
    assertEquals(1f, fieldResolver.fromSqlValue(helperA.getFieldDescriptor("float"), 1));
    assertEquals(1f, fieldResolver.fromSqlValue(helperA.getFieldDescriptor("float"), 1f));
    assertEquals(1d, fieldResolver.fromSqlValue(helperA.getFieldDescriptor("double"), 1));
    assertEquals(1.0, fieldResolver.fromSqlValue(helperA.getFieldDescriptor("double"), 1.0));
    assertEquals(true, fieldResolver.fromSqlValue(helperA.getFieldDescriptor("bool"), 1));
    assertEquals(false, fieldResolver.fromSqlValue(helperA.getFieldDescriptor("bool"), 0));
    assertEquals(TestModel.EnumA.EA0.getValueDescriptor(),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("enuma"), 0));
    ByteString bytes = ByteString.copyFrom(getClass().getName().getBytes());
    assertEquals(bytes,
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("bytes"), bytes.toStringUtf8()));
    assertEquals(TestModel.MsgB.getDefaultInstance(),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("msgb"), ""));

    try {
      fieldResolver.fromSqlValue(helperA.getFieldDescriptor("bool"), 1.0);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  @SuppressWarnings("rawtype")
  public void testRepeatedFieldFromSqlValue() {

    List enumas = (List) fieldResolver.fromSqlValue(helperA.getFieldDescriptor("enuma_arr"), "0,2");
    assertEquals(2, enumas.size());
    assertTrue(enumas.get(0) instanceof Descriptors.EnumValueDescriptor);
    assertEquals(TestModel.EnumA.EA0.name(),
        ((Descriptors.EnumValueDescriptor) enumas.get(0)).getName());

    assertEquals(Lists.newArrayList(1),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("int32_arr"), "1,"));
    assertEquals(Lists.newArrayList(1L),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("int64_arr"), "1"));
    assertEquals(Lists.newArrayList(1f),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("float_arr"), "1"));
    assertEquals(Lists.newArrayList(1.0),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("double_arr"), "1"));
    assertEquals(Lists.newArrayList(true),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("bool_arr"), "1"));
    assertEquals(Lists.newArrayList(false),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("bool_arr"), "0"));
    List list = (List) fieldResolver.fromSqlValue(helperA.getFieldDescriptor("bytes_arr"), ",");
    assertEquals(1, list.size());
    assertEquals(ByteString.copyFromUtf8(""), list.get(0));

    assertEquals(Lists.newArrayList(),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("string_arr"), null));
    assertEquals(Lists.newArrayList(),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("string_arr"), ""));
    assertEquals(Lists.newArrayList(""),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("string_arr"), ","));
    assertEquals(Lists.newArrayList("a"),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("string_arr"), "a"));
    assertEquals(Lists.newArrayList(","),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("string_arr"), "%2c,"));
    assertEquals(Lists.newArrayList(","),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("string_arr"), "%2c"));
    assertEquals(Lists.newArrayList("%"),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("string_arr"), "%25,"));

    try {
      fieldResolver.fromSqlValue(helperA.getFieldDescriptor("int32_arr"), 1);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
    try {
      fieldResolver.fromSqlValue(helperA.getFieldDescriptor("msgb_arr"), "a");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testMapFieldFromSqlValue() {
    assertEquals(Collections.emptyList(),
        fieldResolver.fromSqlValue(helperA.getFieldDescriptor("int32_map"), null));

    List<MapEntry> mapEntries;

    mapEntries =
        (List<MapEntry>) fieldResolver.fromSqlValue(helperA.getFieldDescriptor("int64_map"), "");
    assertEquals(0, mapEntries.size());

    mapEntries = (List<MapEntry>) fieldResolver
        .fromSqlValue(helperA.getFieldDescriptor("int64_map"), "{\"a\":1}");
    assertEquals(1, mapEntries.size());
    assertEquals("a", mapEntries.get(0).getKey());
    assertEquals(1L, mapEntries.get(0).getValue());

    mapEntries = (List<MapEntry>) fieldResolver
        .fromSqlValue(helperA.getFieldDescriptor("int64_map"), "{\"a\":1}");
    assertEquals(1, mapEntries.size());
    assertEquals("a", mapEntries.get(0).getKey());
    assertEquals(1L, mapEntries.get(0).getValue());

    mapEntries = (List<MapEntry>) fieldResolver
        .fromSqlValue(helperA.getFieldDescriptor("bytes_map"), "{\"a\":1}");
    assertEquals(1, mapEntries.size());
    assertEquals("a", mapEntries.get(0).getKey());
    assertEquals(ByteString.copyFromUtf8("1"), mapEntries.get(0).getValue());

    try {
      fieldResolver.fromSqlValue(helperA.getFieldDescriptor("int32_map"), 1);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
    try {
      fieldResolver.fromSqlValue(helperA.getFieldDescriptor("msgb_map"), "{\"a\":1}");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

}
