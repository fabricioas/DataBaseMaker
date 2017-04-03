/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function createTableFrame(table, x0, y0, fontSize) {
   var maxLength = table.name.length;
   for (i = 0; i < table.attributes.length; i++) {
      if (table.attributes[i].length > maxLength) {
	 maxLength = table.attributes[i].length;
      }
   }
   var width = (maxLength + 3) * fontSize * 0.625;
   var height = (table.attributes.length + 1) * fontSize * 1.2 + fontSize / 2;
   return {"x0": x0, "y0": y0, "width": width, "height": height};
}

function drawTable(ctx, table, tableFrame, font, fontSize, background, color) {
   var x0 = tableFrame.x0;
   var y0 = tableFrame.y0;
   var width = tableFrame.width;
   var height = tableFrame.height;

   var shadow = createShadowObject(fontSize, fontSize * 0.2, fontSize * 0.2, "gray");
   drawRect(ctx, x0, y0, width, height, background, color, shadow);
   drawText(ctx, table.name, font, fontSize, "bold", null, x0 + width / 2, y0 + fontSize, null, "center", color);
   drawLine(ctx, x0, y0 + fontSize * 1.4, x0 + width, y0 + fontSize * 1.4);
   for (i = 0; i < table.attributes.length; i++) {
      drawText(ctx, "» " + table.attributes[i], font, fontSize, "lighter", null, x0 + 10, y0 + fontSize * 1.2 * (i + 2), null, "left", color);
   }
}