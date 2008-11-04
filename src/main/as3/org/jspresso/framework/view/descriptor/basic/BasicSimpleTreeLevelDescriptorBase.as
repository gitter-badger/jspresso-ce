/**
 * Generated by Gas3 v1.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERRIDDEN EACH TIME YOU USE
 * THE GENERATOR. CHANGE INSTEAD THE INHERITED CLASS (BasicSimpleTreeLevelDescriptor.as).
 */

package org.jspresso.framework.view.descriptor.basic {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import org.jspresso.framework.view.descriptor.ISimpleTreeLevelDescriptor;
    import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;

    [Bindable]
    public class BasicSimpleTreeLevelDescriptorBase extends BasicTreeLevelDescriptor implements ISimpleTreeLevelDescriptor {

        private var _childDescriptor:ITreeLevelDescriptor;

        public function set childDescriptor(value:ITreeLevelDescriptor):void {
            _childDescriptor = value;
        }
        public function get childDescriptor():ITreeLevelDescriptor {
            return _childDescriptor;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _childDescriptor = input.readObject() as ITreeLevelDescriptor;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_childDescriptor);
        }
    }
}