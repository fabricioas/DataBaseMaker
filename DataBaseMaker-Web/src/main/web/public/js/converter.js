/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var NODE_ELEMENT = 1;
var TEXT_ELEMENT = 3;

function trim(text){
  return text.replace(/^\s+|\s+$/gm,'');
}

var xmlStringToJson = function (xmlText) {
   var parser = new DOMParser();
//   console.log(trim(xmlText).replace("'\n'", ''));
   var xmlDoc = parser.parseFromString(trim(xmlText).replace("\n", ''), "text/xml");
   return xmlToJson(xmlDoc);
};

var xmlToJson = function (node) {
   var obj = {};
   if (node.nodeType === NODE_ELEMENT) {
      if (node.attributes.length > 0) {
	 obj["@attributes"] = {};
	 for (var j = 0; j < node.attributes.length; j++) {
	    var attribute = node.attributes.item(j);
	    obj["@attributes"][attribute.nodeName] = attribute.nodeValue;
	 }
      }
   } else if (node.nodeType === TEXT_ELEMENT) {
      obj = node.nodeValue;
   }
   if (node.hasChildNodes()) {
      for (var i = 0; i < node.childNodes.length; i++) {
	 var item = node.childNodes.item(i);
	 var nodeName = item.nodeName;
	 if (typeof (obj[nodeName]) === "undefined") {
	    obj[nodeName] = xmlToJson(item);
	 } else {
	    if (typeof (obj[nodeName].push) === "undefined") {
	       var old = obj[nodeName];
	       obj[nodeName] = [];
	       obj[nodeName].push(old);
	    }
	    obj[nodeName].push(xmlToJson(item));
	 }
      }
   }
   return obj;
};

//var teste = {"metadados": {
//      "#text": ["\n   ", "\n"], "table": {"attributes": [{"name": "DOMINIO"}, {"#text": ["\n\t ", "\n\t ", "\n\t ", "\n\t ", "\n\t ", "\n\t ", "\n\t ", "\n\t ", "\n\t ", "\n\t ", "\n\t ", "\n\t ", "\n      "], "attribute": [{"attributes": {"type": "PK", "nullable": "false"}, "#text": ["\n\t    ", "\n\t    ", "\n\t    ", "\n\t    ", "\n\t    ", "\n\t "], "name": {"#text": "ID_DOMINIO"}, "constraint": {"attributes": {"name": "PK_DOMINIO"}}, "type": {"#text": "NUMBER"}, "size": {"#text": "38"}, "comment": {"#text": "Identificador unico da tabela DOMINIO."}}, {"attributes": {"nullable": "false"}, "#text": ["\n\t    ", "\n\t    ", "\n\t    ", "\n\t    ", "\n\t "], "name": {"#text": "DESCRICAO_DOMINIO"}, "type": {"#text": "VARCHAR"}, "size": {"#text": "100"}, "comment": {"#text": "Identificacao textural do dominio."}}, {"attributes": {"nullable": "false"}, "#text": ["\n\t    ", "\n\t    ", "\n\t    ", "\n\t "], "name": {"#text": "DATA_CRIACAO"}, "type": {"#text": "TIMESTAMP"}, "comment": {"#text": "Data em que o registro foi criado."}}, {"attributes": {"type": "FK", "nullable": "false"}, "#text": ["\n\t    ", "\n\t    ", "\n\t    ", "\n\t    ", "\n\t    ", "\n\t "], "name": {"#text": "ID_USUARIO_CRIACAO"}, "constraint": {"attributes": {"name": "FK_USUARIO_CRIACAO"}, "#text": ["\n\t       ", "\n\t       ", "\n\t    "], "referenceTable": {"#text": "USUARIO"}, "referenceId": {"#text": "ID_USUARIO"}}, "type": {"#text": "NUMBER"}, "size": {"#text": "38"}, "comment": {"#text": "Identificador unico da tabela USUARIO e que mantem usuario responsavel pela criacao do dominio."}}, {"attributes": {"nullable": "true"}, "#text": ["\n\t    ", "\n\t    ", "\n\t    ", "\n\t "], "name": {"#text": "DATA_ALTERACAO"}, "type": {"#text": "TIMESTAMP"}, "comment": {"#text": "Data em que o registro foi alterado."}}, {"attributes": {"type": "FK", "nullable": "false"}, "#text": ["\n\t    ", "\n\t    ", "\n\t    ", "\n\t    ", "\n\t    ", "\n\t "], "name": {"#text": "ID_USUARIO_ALTERACAO"}, "constraint": {"attributes": {"name": "FK_USUARIO_ALTERACAO"}, "#text": ["\n\t       ", "\n\t       ", "\n\t    "], "referenceTable": {"#text": "USUARIO"}, "referenceId": {"#text": "ID_USUARIO"}}, "type": {"#text": "NUMBER"}, "size": {"#text": "38"}, "comment": {"#text": "Identificador unico da tabela USUARIO e que mantem usuario responsavel pela alteracao do dominio."}}, {"attributes": {"nullable": "false"}, "#text": ["\n\t    ", "\n\t    ", "\n\t    ", "\n\t    ", "\n\t    ", "\n\t    ", "\n\t "], "name": {"#text": "STATUS_DOMINIO"}, "type": {"#text": "VARCHAR"}, "default": {"#text": "P"}, "size": {"#text": "1"}, "comment": {"#text": "Status atual do registro de dominio (P=Pendente, A=Ativo ou I=Inativo)."}, "constraint": {"attributes": {"name": "CK_STATUSDOMINIO"}, "#text": ["\n\t       ", "\n\t    "], "checkValues": {"#text": ["\n\t\t  ", "\n\t\t  ", "\n\t\t  ", "\n\t       "], "value": [{"#text": "P"}, {"#text": "A"}, {"#text": "I"}]}}}, {"attributes": {"nullable": "true"}, "#text": ["\n\t    ", "\n\t    ", "\n\t    ", "\n\t "], "name": {"#text": "DAT_ATUALIZACAO"}, "type": {"#text": "TIMESTAMP"}, "comment": {"#text": "Data em que o registro foi atualizado."}}, {"attributes": {"type": "FK", "nullable": "false"}, "#text": ["\n\t    ", "\n\t    ", "\n\t    ", "\n\t    ", "\n\t    ", "\n\t "], "name": {"#text": "ID_USUARIO_DESATIVACAO"}, "constraint": {"attributes": {"name": "FK_USUARIO_DESATIVACAO"}, "#text": ["\n\t       ", "\n\t       ", "\n\t    "], "referenceTable": {"#text": "USUARIO"}, "referenceId": {"#text": "ID_USUARIO"}}, "type": {"#text": "NUMBER"}, "size": {"#text": "38"}, "comment": {"#text": "Identificador unico da tabela USUARIO e que mantem usuario responsavel pela desativacao do dominio."}}], "#comment": [{}, {}, {}]}], "#text": ["\n      ", "\n      ", "\n      ", "\n      ", "\n      ", "\n      ", "\n   "], "owner": {"#text": "OWNER"}, "comment": {"#text": "Tabela que mantem informacoes dos tipos de dados de dominio. ALIAS: DOMINIO"}, "pk": {"#text": "PK_DOMINIO"}, "sequence": {"#text": "SEQ_DOMINIO_OID"}, "trigger": {"#text": ["\n\t ", "\n\t ", "\n      "], "name": {"#text": "TRG_DOMINIO_OID"}, "comment": {"#text": "Padrao para controle do oid."}}}}};