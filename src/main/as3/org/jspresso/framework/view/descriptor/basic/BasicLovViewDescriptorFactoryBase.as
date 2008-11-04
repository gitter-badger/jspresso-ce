/**
 * Generated by Gas3 v1.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERRIDDEN EACH TIME YOU USE
 * THE GENERATOR. CHANGE INSTEAD THE INHERITED CLASS (BasicLovViewDescriptorFactory.as).
 */

package org.jspresso.framework.view.descriptor.basic {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import flash.utils.IExternalizable;
    import org.jspresso.framework.view.descriptor.ILovViewDescriptorFactory;
    import org.jspresso.framework.view.descriptor.IQueryViewDescriptorFactory;

    [Bindable]
    public class BasicLovViewDescriptorFactoryBase implements IExternalizable, ILovViewDescriptorFactory {

        private var _queryViewDescriptorFactory:IQueryViewDescriptorFactory;

        public function set queryViewDescriptorFactory(value:IQueryViewDescriptorFactory):void {
            _queryViewDescriptorFactory = value;
        }

        public function readExternal(input:IDataInput):void {
            _queryViewDescriptorFactory = input.readObject() as IQueryViewDescriptorFactory;
        }

        public function writeExternal(output:IDataOutput):void {
            output.writeObject(_queryViewDescriptorFactory);
        }
    }
}