package org.jspresso.framework.view.flex
{
  import flash.events.FocusEvent;
  
  import mx.controls.TextInput;
  import mx.core.IFlexDisplayObject;
  import mx.core.mx_internal;
  import mx.managers.IFocusManager;

  use namespace mx_internal;

  public class EnhancedTextInput extends TextInput {
    
    private var _preventDefaultButton:Boolean = false;
         
    public function EnhancedTextInput() {
      super();
    }

    override protected function focusInHandler(event:FocusEvent):void {
      super.focusInHandler(event);
      if(preventDefaultButton) {
        var fm:IFocusManager = focusManager;
        if (fm) {
          fm.defaultButtonEnabled = false;
        }
      }
    }
    
    override protected function focusOutHandler(event:FocusEvent):void {
      super.focusOutHandler(event);
      var fm:IFocusManager = focusManager;
      if (fm) {
        fm.defaultButtonEnabled = true;
      }
    }

    public function get preventDefaultButton():Boolean
    {
      return _preventDefaultButton;
    }

    public function set preventDefaultButton(value:Boolean):void
    {
      _preventDefaultButton = value;
    }

  }
}