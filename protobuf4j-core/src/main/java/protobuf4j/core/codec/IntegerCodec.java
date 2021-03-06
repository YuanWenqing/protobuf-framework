package protobuf4j.core.codec;

import javax.annotation.Nullable;

/**
 * Created by tuqc on 15-5-24.
 */
public class IntegerCodec implements ICodec<Integer> {
  public static final IntegerCodec INSTANCE = new IntegerCodec();

  private IntegerCodec() {
  }

  @Override
  public Class<Integer> getValueType() {
    return Integer.class;
  }

  private StringCodec nativeCodec() {
    return StringCodec.INSTANCE;
  }

  @Override
  public Integer decode(@Nullable byte[] data) {
    if (data == null) return null;
    return Integer.valueOf(nativeCodec().decode(data));
  }

  @Override
  public byte[] encode(@Nullable Integer v) {
    if (v == null) {
      return null;
    }
    return nativeCodec().encode(String.valueOf(v));
  }
}
