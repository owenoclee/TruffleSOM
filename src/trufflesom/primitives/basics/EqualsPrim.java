package trufflesom.primitives.basics;

import java.math.BigInteger;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.Specialization;

import bd.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.BinaryExpressionNode.BinarySystemOperation;
import trufflesom.vm.Universe;
import trufflesom.vmobjects.SObject;
import trufflesom.vmobjects.SSymbol;


@Primitive(className = "Integer", primitive = "=")
@Primitive(className = "Double", primitive = "=")
@Primitive(className = "String", primitive = "=")
@Primitive(selector = "=")
public abstract class EqualsPrim extends BinarySystemOperation {

  @CompilationFinal private SObject trueObject;
  @CompilationFinal private SObject falseObject;

  @Override
  public BinarySystemOperation initialize(final Universe universe) {
    super.initialize(universe);
    this.trueObject = universe.getTrueObject();
    this.falseObject = universe.getFalseObject();
    return this;
  }

  @Specialization
  public final boolean doBoolean(final boolean left, final boolean right) {
    return left == right;
  }

  @Specialization
  public final boolean doBoolean(final boolean left, final SObject right) {
    return (left && right == trueObject) ||
        (!left && right == falseObject);
  }

  @Specialization
  public final boolean doLong(final long left, final long right) {
    return left == right;
  }

  @Specialization
  public final boolean doBigInteger(final BigInteger left, final BigInteger right) {
    return left.compareTo(right) == 0;
  }

  @Specialization
  public final boolean doString(final String receiver, final String argument) {
    return receiver.equals(argument);
  }

  @Specialization
  public final boolean doDouble(final double left, final double right) {
    return left == right;
  }

  @Specialization
  public final boolean doSSymbol(final SSymbol left, final SSymbol right) {
    return left == right;
  }

  @Specialization
  public final boolean doLong(final long left, final double right) {
    return left == right;
  }

  @Specialization
  public final boolean doBigInteger(final BigInteger left, final long right) {
    return doBigInteger(left, BigInteger.valueOf(right));
  }

  @Specialization
  public final boolean doLong(final long left, final BigInteger right) {
    return doBigInteger(BigInteger.valueOf(left), right);
  }

  @Specialization
  public final boolean doDouble(final double left, final long right) {
    return doDouble(left, (double) right);
  }

  @Specialization
  public final boolean doString(final String receiver, final SSymbol argument) {
    return receiver.equals(argument.getString());
  }

  @Specialization
  public final boolean doSSymbol(final SSymbol receiver, final String argument) {
    return receiver.getString().equals(argument);
  }

  @Specialization
  public final boolean doLong(final long left, final String right) {
    return false;
  }

  @Specialization
  public final boolean doLong(final long left, final SObject right) {
    return false;
  }

  @Specialization
  public final boolean doLong(final long left, final SSymbol right) {
    return false;
  }

  @Specialization
  public final boolean doString(final String receiver, final long argument) {
    return false;
  }

  @Specialization
  public final boolean doString(final String receiver, final SObject argument) {
    return false;
  }

  @Specialization
  public final boolean doSSymbol(final SSymbol receiver, final long argument) {
    return false;
  }

  @Specialization
  public final boolean doSSymbol(final SSymbol receiver, final SObject argument) {
    return false;
  }
}
