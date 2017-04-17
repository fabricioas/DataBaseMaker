/* 
 * Definições de entidades de domínio
 */

function table(name) {
   this.name = name;
   this.attributes = new Array();
   this.addAttr = function (attribute) {
      this.attributes.push(attribute);
   };
   this.computeWidth = function(){
      var maxLength = this.name.length;
      for (i = 0; i < this.attributes.length; i++) {
	 if (this.attributes[i].length > maxLength) {
	    maxLength = this.attributes[i].length;
	 }
      }
      return maxLength;
   };
   this.computeHeight = function(){
      return this.attributes.length + 1;
   };
}

function node(id, info, linkTo) {
   this.id = id;
   this.info = info;
   this.refCount = 0;
   this.linksTo = new Array();
   this.linksFrom = new Array();


   this.addLinkTo = function (linkTo) {
      this.linksTo.push(linkTo);
      linkTo.refCount++;
      linkTo.linksFrom.push(this);
   };

   if (linkTo !== null) {
      this.addLinkTo(linkTo);
   }

   this.hasLinksTo = function () {
      return (this.linksTo.length > 0);
   };

   this.hasLinksFrom = function () {
      return (this.linksFrom.length > 0);
   };

   this.printLinkTo = function (tab) {
      var out = tab + this.info.name + " (" + this.refCount + ")<br/>";
      if (this.hasLinksTo()) {
	 for (i = 0; i < this.linksTo.length; i++) {
	    out += this.linksTo[i].printLinkTo(tab + "&nbsp;&nbsp;&nbsp;&nbsp;");
	 }
      }
      return out;
   };

   this.printLinkFrom = function (tab) {
      var out = tab + this.info.name + " (" + this.refCount + "). Link to: [";
      var aux = "";
      for (j = 0; j < this.linksTo.length; j++) {
	 out += aux + this.linksTo[j].info.name;
	 aux = ", ";
      }
      out += "]<br/>";
      if (this.hasLinksFrom()) {
	 for (i = 0; i < this.linksFrom.length; i++) {
	    out += this.linksFrom[i].printLinkFrom(tab + "&nbsp;&nbsp;&nbsp;&nbsp;");
	 }
      }
      return out;
   };
}

function sort(list, field) {
   for (i = 0; i < list.length; i++) {
      var k = i;
      for (j = list.length - 1; j > i; j--) {
	 if (list[j][field] < list[k][field]) {
	    k = j;
	 }
      }
      var temp = list[k];
      list[k] = list[i];
      list[i] = temp;
   }
}

function sortBy(list, field, comparator) {
   for (i = 0; i < list.length; i++) {
      var k = i;
      for (j = list.length - 1; j > i; j--) {
	 if (comparator(list[j][field], list[k][field])) {
	    k = j;
	 }
      }
      var temp = list[k];
      list[k] = list[i];
      list[i] = temp;
   }
}

function lessThan(value1, value2) {
   return value1 < value2;
}

function greaterThan(value1, value2) {
   return value1 > value2;
}