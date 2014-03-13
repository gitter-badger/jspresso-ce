/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 */

qx.Class.define("org.jspresso.framework.view.qx.mobile.DatePicker", {
  extend: qx.ui.mobile.dialog.Picker,

  statics: {
  },

  construct: function (monthNames) {
    this.base(arguments);
    this.__monthNames = monthNames;
    this.__pickerDaySlotData = this._createDayPickerSlot(1, new Date().getFullYear());

    addSlot(this.__pickerDaySlotData);
    addSlot(this._createMonthPickerSlot());
    addSlot(this._createYearPickerSlot());
  },

  members: {

    __monthNames :null,
    __pickerDaySlotData : null,

    /**
     * Creates the picker slot data for days in month.
     * @param month {Integer} current month.
     * @param year {Integer} current year.
     */
    _createDayPickerSlot : function(month, year) {
      var daysInMonth = new Date(year, month + 1, 0).getDate();

      var slotData = [];
      for (var i = 1; i <= daysInMonth; i++) {
        slotData.push("" + i);
      }
      return new qx.data.Array(slotData);
    },


    /**
     * Creates the picker slot data for month names, based on current locale settings.
     */
    _createMonthPickerSlot : function() {
      var names = this.__monthNames;
      var slotData = [];
      for (var i = 0; i < names.length; i++) {
        slotData.push("" + names[i]);
      }
      return new qx.data.Array(slotData);
    },


    /**
     * Creates the picker slot data from 1950 till current year +50.
     */
    _createYearPickerSlot : function() {
      var slotData = [];
      for (var i = new Date().getFullYear() + 50; i > 1950; i--) {
        slotData.push("" + i);
      }
      return new qx.data.Array(slotData);
    },

    /**
     * Updates the shown days in the picker slot.
     */
    _updatePickerDaySlot : function() {
      var dayIndex = this.getSelectedIndex(0);
      var monthIndex = this.getSelectedIndex(1);
      var yearIndex = this.getSelectedIndex(2);
      var slotData = this._createDayPickerSlot(monthIndex, new Date().getFullYear() - yearIndex);
      this.__pickerDaySlotData.removeAll();
      this.__pickerDaySlotData.append(slotData);

      this.setSelectedIndex(0, dayIndex, false);
    }
  }
});
