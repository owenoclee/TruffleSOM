package trufflesom.primitives.arithmetic;

import java.math.BigInteger;

import trufflesom.interpreter.nodes.nary.BinaryExpressionNode;


public abstract class ArithmeticPrim extends BinaryExpressionNode {
  protected final Number reduceToLongIfPossible(final BigInteger result) {
    if (result.bitLength() > Long.SIZE - 1) {
      return result;
    } else {
      return result.longValue();
    }
  }
}
