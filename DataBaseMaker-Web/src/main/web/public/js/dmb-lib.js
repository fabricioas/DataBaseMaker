/* 
 * Funções do app para desenho
 */

function createTableFrame(table, x0, y0, fontSize) {
   var maxLength = table.name.length;
   for (i = 0; i < table.attributes.length; i++) {
      if (table.attributes[i].length > maxLength) {
	 maxLength = table.attributes[i].length;
      }
   }
   var maxLength = table.computeWidth();
   var width = (maxLength + 3) * fontSize * 0.625;
   var height = (table.attributes.length + 1) * fontSize * 1.2 + fontSize / 2;
   return {"x0": x0, "y0": y0, "width": width, "height": height};
}

function drawTable(ctx, table, tableFrame, settings) {
   var x0 = tableFrame.x0;
   var y0 = tableFrame.y0;
   var width = tableFrame.width;
   var height = tableFrame.height;
   var fontSize = settings.fontSize;
   var font = settings.font;
   var background = settings.backgroundColor;
   var color = settings.foregroundColor;

   var shadow = createShadowObject(fontSize, fontSize * 0.2, fontSize * 0.2, "gray");
   drawRect(ctx, x0, y0, width, height, background, color, shadow);
   drawText(ctx, table.name, font, fontSize, "bold", null, x0 + width / 2, y0 + fontSize, null, "center", color);
   drawLine(ctx, x0, y0 + fontSize * 1.4, x0 + width, y0 + fontSize * 1.4);
   for (i = 0; i < table.attributes.length; i++) {
      drawText(ctx, "» " + table.attributes[i], font, fontSize, "lighter", null, x0 + 10, y0 + fontSize * 1.2 * (i + 2), null, "left", color);
   }
}

function getNextNodes(nodes, startIndex) {
   var refNode = nodes[startIndex];
   var arr = new Array(refNode);
   for (var i = startIndex + 1; i < nodes.length; i++) {
      if (nodes[i].refCount === refNode.refCount) {
	 arr.push(nodes[i]);
      }
   }
   return arr;
}

function computeFrame(center, width, height, current, nodes, level, settings) {
   var frame = {
      "x0": 0,
      "y0": 0,
      "width": 0,
      "height": 0
   };

   var w = width * (level === 1 && nodes.length === 1 ? 0 : level - 1) * 0.8;
   var h = height * (level === 1 && nodes.length === 1 ? 0 : level - 1) * 0.8;
   var size = Math.max(w, h);
   var dx = width / 2;
   var dy = height / 2;
   var step = TWO_PI * (current / nodes.length) + (HALF_PI * ((level - 1) % 2));
   frame.x0 = center.x + Math.cos(step) * size - dx;
   frame.y0 = center.y + Math.sin(step) * size - dy;

   frame.width = width;
   frame.height = nodes[current].info.computeHeight() * settings.fontSize * 1.2
		 + settings.fontSize / 2;
   return frame;
}


function lessThan(value1, value2) {
   return value1 < value2;
}

function greaterThan(value1, value2) {
   return value1 > value2;
}

function drawNodes(startPos, ctx, nodes, settings) {
   var startIndex = 0;
   var level = 1;
   var width, height;
   var frames = new Array();
   while (startIndex < nodes.length) {
      var next = getNextNodes(nodes, startIndex);
      startIndex += next.length;
      if (!width) {
	 var maxLength = 0;
	 var maxHeight = 0;
	 for (var i = 0; i < nodes.length; i++) {
	    var length = nodes[i].info.computeWidth();
	    if (length > maxLength) {
	       maxLength = length;
	    }
	    var hght = nodes[i].info.computeHeight();
	    if (hght > maxHeight) {
	       maxHeight = hght;
	    }

	 }

//	 var width = (maxLength + 3) * fontSize * 0.625;
//         var height = (table.attributes.length + 1) * fontSize * 1.2 + fontSize / 2;
	 width = (maxLength + 2)
		 * settings.fontSize * 0.675;
	 height = maxHeight
		 * settings.fontSize * 1.2
		 + settings.fontSize / 2;
      }
      for (var i = 0; i < next.length; i++) {
	 var frame = computeFrame(startPos, width, height, i, next, level, settings);
	 frames[next[i].id] = frame;
      }
      level++;
   }

   for (var i = 0; i < nodes.length; i++) {
      drawTable(ctx, nodes[i].info, frames[nodes[i].id], settings);
   }

   for (var i = nodes.length - 1; i > 0; i--) {
      var linksTo = nodes[i].linksTo;
      for (var j = 0; j < linksTo.length; j++) {
	 drawLink(ctx, frames[nodes[i].id], frames[linksTo[j].id]);
      }
   }
}

function drawDiagramFromXML(nodeList, canvas, settings) {
   var pos = {
      "x": canvas.width / 2,
      "y": canvas.height / 2
   };
   sortBy(nodeList, "refCount", greaterThan);
   for (var i = 0; i < nodeList.length; i++) {
      console.log(nodeList[i].info.name + " -> " + nodeList[i].refCount);
   }
   drawNodes(pos, canvas.getContext("2d"), nodeList, settings);
}

function createTableNodesHierarchy(jsonFromXML) {
   var tablesObj = (jsonFromXML["metadados"])["tables"]["table"];
   var tables = [];
   var tablesKey = [];
   var nodeList = [];
   var nodesKey = [];
   var nextId = 1;
   for (var i = 0; i < tablesObj.length; i++) {
      var tableObj = tablesObj[i];
      var attributesObj = tableObj["@attributes"];
      var info = new table(attributesObj["name"]);
      tables.push(info);
      tablesKey[info.name] = info;
      var nd = new node(nextId++, info, null);
      nodeList.push(nd);
      nodesKey[info.name] = nd;
      var attrs = tableObj["attributes"]["attribute"];
      for (var j = 0; j < attrs.length; j++) {
	 info.addAttr(attrs[j].name["#text"]);
      }
   }
   for (var i = 0; i < tablesObj.length; i++) {
      var tableObj = tablesObj[i];
      var attributesObj = tableObj["@attributes"];
      var info = tablesKey[attributesObj["name"]];
      var attrs = tableObj["attributes"]["attribute"];
      var tblNode = nodesKey[info.name];
      for (var j = 0; j < attrs.length; j++) {
	 if ((typeof(attrs[j].constraint) !== "undefined") 
		 && (typeof(attrs[j].constraint.referenceTable) !== "undefined")) {
	    var tblName = attrs[j].constraint.referenceTable["#text"];
	    var tblField = attrs[j].constraint.referenceId["#text"];
	    var fkTbl = tablesKey[tblName];
	    if (!fkTbl) {
	       fkTbl = new table(tblName);
	       fkTbl.addAttr(tblField);
	       tables.push(fkTbl);
	       tablesKey[tblName] = fkTbl;
	       var nd = new node(nextId++, fkTbl, null);
	       nodeList.push(nd);
	       nodesKey[fkTbl.name] = nd;
	    }
	    tblNode.addLinkTo(nodesKey[tblName]);
	 }
      }
   }
   return nodeList;
}