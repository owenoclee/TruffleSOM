package som.primitives.reflection;

import som.interpreter.SArguments;
import som.interpreter.nodes.nary.QuaternaryExpressionNode;
import som.vmobjects.SArray;
import som.vmobjects.SClass;
import som.vmobjects.SInvokable;
import som.vmobjects.SObject;
import som.vmobjects.SSymbol;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class PerformWithArgumentsInSuperclassPrim extends QuaternaryExpressionNode {
  public PerformWithArgumentsInSuperclassPrim() { super(null, false); } /* TODO: enforced!!! */

  @Specialization
  public final Object doSAbstractObject(final VirtualFrame frame,
      final Object receiver, final SSymbol selector,
      final Object[] argArr, final SClass clazz) {
    CompilerAsserts.neverPartOfCompilation();
    SInvokable invokable = clazz.lookupInvokable(selector);
    SObject domain = SArguments.domain(frame);
    boolean enforced = SArguments.enforced(frame);
    return invokable.invoke(domain, enforced, SArray.fromSArrayToArgArrayWithReceiver(argArr, receiver));
  }

  public abstract static class PerformEnforcedWithArgumentsInSuperclassPrim extends QuaternaryExpressionNode {
    @Child private AbstractSymbolSuperDispatch dispatch;

    public PerformEnforcedWithArgumentsInSuperclassPrim() {
      super(null, true);
      dispatch = AbstractSymbolSuperDispatch.create(true);
    }

    @Specialization
    public final Object doSAbstractObject(final VirtualFrame frame,
        final Object receiver, final SSymbol selector, final Object[] argArr, final SClass clazz) {
      CompilerAsserts.neverPartOfCompilation();
      return dispatch.executeDispatch(frame, receiver, selector, clazz, argArr);
    }
  }
}