package trufflesom.primitives.reflection;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

import bd.primitives.Primitive;
import trufflesom.interpreter.Types;
import trufflesom.interpreter.nodes.nary.BinaryExpressionNode;
import trufflesom.interpreter.nodes.nary.BinaryExpressionNode.BinarySystemOperation;
import trufflesom.interpreter.nodes.nary.TernaryExpressionNode.TernarySystemOperation;
import trufflesom.interpreter.nodes.nary.UnaryExpressionNode;
import trufflesom.interpreter.nodes.nary.UnaryExpressionNode.UnarySystemOperation;
import trufflesom.vm.Universe;
import trufflesom.vm.constants.Nil;
import trufflesom.vmobjects.SAbstractObject;
import trufflesom.vmobjects.SClass;
import trufflesom.vmobjects.SObject;
import trufflesom.vmobjects.SSymbol;


public final class ObjectPrims {

  @Primitive(className = "Object", primitive = "instVarAt:", selector = "instVarAt:")
  public abstract static class InstVarAtPrim extends BinarySystemOperation {
    @Child private IndexDispatch dispatch;

    @Override
    public BinarySystemOperation initialize(final Universe universe) {
      super.initialize(universe);
      dispatch = IndexDispatch.create(universe);
      return this;
    }

    @Specialization
    public final Object doSObject(final SObject receiver, final long idx) {
      return dispatch.executeDispatch(receiver, (int) idx - 1);
    }

    @Override
    public final Object executeEvaluated(final VirtualFrame frame,
        final Object receiver, final Object firstArg) {
      assert receiver instanceof SObject;
      assert firstArg instanceof Long;

      SObject rcvr = (SObject) receiver;
      long idx = (long) firstArg;
      return doSObject(rcvr, idx);
    }
  }

  @GenerateNodeFactory
  @Primitive(className = "Object", primitive = "instVarAt:put:", selector = "instVarAt:put:")
  public abstract static class InstVarAtPutPrim extends TernarySystemOperation {
    @Child private IndexDispatch dispatch;

    @Override
    public TernarySystemOperation initialize(final Universe universe) {
      super.initialize(universe);
      dispatch = IndexDispatch.create(universe);
      return this;
    }

    @Specialization
    public final Object doSObject(final SObject receiver, final long idx, final Object val) {
      dispatch.executeDispatch(receiver, (int) idx - 1, val);
      return val;
    }

    @Override
    public final Object executeEvaluated(final VirtualFrame frame,
        final Object receiver, final Object firstArg, final Object secondArg) {
      assert receiver instanceof SObject;
      assert firstArg instanceof Long;
      assert secondArg != null;

      SObject rcvr = (SObject) receiver;
      long idx = (long) firstArg;
      return doSObject(rcvr, idx, secondArg);
    }
  }

  @GenerateNodeFactory
  @Primitive(className = "Object", primitive = "instVarNamed:", selector = "instVarNamed:")
  public abstract static class InstVarNamedPrim extends BinaryExpressionNode {
    @Specialization
    public final Object doSObject(final SObject receiver, final SSymbol fieldName) {
      CompilerAsserts.neverPartOfCompilation();
      return receiver.getField(receiver.getFieldIndex(fieldName));
    }
  }

  @GenerateNodeFactory
  @Primitive(className = "Object", primitive = "halt")
  public abstract static class HaltPrim extends UnaryExpressionNode {
    @Specialization
    public final Object doSAbstractObject(final Object receiver) {
      Universe.errorPrintln("BREAKPOINT");
      return receiver;
    }
  }

  @GenerateNodeFactory
  @Primitive(className = "Object", primitive = "class")
  public abstract static class ClassPrim extends UnarySystemOperation {
    @Specialization
    public final SClass doSAbstractObject(final SAbstractObject receiver) {
      return receiver.getSOMClass(universe);
    }

    @Specialization
    public final SClass doObject(final Object receiver) {
      return Types.getClassOf(receiver, universe);
    }
  }

  @GenerateNodeFactory
  @Primitive(selector = "isNil", noWrapper = true)
  public abstract static class IsNilNode extends UnaryExpressionNode {
    @Specialization
    public final boolean isNil(final Object receiver) {
      return receiver == Nil.nilObject;
    }
  }

  @GenerateNodeFactory
  @Primitive(selector = "notNil", noWrapper = true)
  public abstract static class NotNilNode extends UnaryExpressionNode {
    @Specialization
    public final boolean notNil(final Object receiver) {
      return receiver != Nil.nilObject;
    }
  }
}
