package trufflesom.primitives.basics;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;

import bd.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.UnaryExpressionNode;
import trufflesom.vmobjects.SAbstractObject;
import trufflesom.vmobjects.SSymbol;


@GenerateNodeFactory
@Primitive(className = "Object", primitive = "hashcode")
@Primitive(className = "String", primitive = "hashcode")
public abstract class HashPrim extends UnaryExpressionNode {
  @Specialization
  public final long doString(final String receiver) {
    return receiver.hashCode();
  }

  @Specialization
  public final long doSSymbol(final SSymbol receiver) {
    return receiver.getString().hashCode();
  }

  @Specialization
  public final long doSAbstractObject(final SAbstractObject receiver) {
    return receiver.hashCode();
  }
}
