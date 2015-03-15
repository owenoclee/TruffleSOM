package som.primitives.arrays;

import som.interpreter.nodes.nary.UnaryExpressionNode;
import som.vmobjects.SArray;
import som.vmobjects.SArray.ArrayType;

import com.oracle.truffle.api.dsl.Specialization;

public abstract class CopyPrim extends UnaryExpressionNode {
  public final static boolean isEmptyType(final SArray receiver) {
    return receiver.getType() == ArrayType.EMPTY;
  }

  public final static boolean isPartiallyEmptyType(final SArray receiver) {
    return receiver.getType() == ArrayType.PARTIAL_EMPTY;
  }

  public final static boolean isObjectType(final SArray receiver) {
    return receiver.getType() == ArrayType.OBJECT;
  }

  @Specialization(guards = "isEmptyType")
  public final SArray doEmptyArray(final SArray receiver) {
    return new SArray(receiver.getEmptyStorage());
  }

  @Specialization(guards = "isPartiallyEmptyType")
  public final SArray doPartiallyEmptyArray(final SArray receiver) {
    return new SArray(ArrayType.PARTIAL_EMPTY, receiver.getPartiallyEmptyStorage().copy());
  }

  @Specialization(guards = "isObjectType")
  public final SArray doObjectArray(final SArray receiver) {
    return SArray.create(receiver.getObjectStorage().clone());
  }
}