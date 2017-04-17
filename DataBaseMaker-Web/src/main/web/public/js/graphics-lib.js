/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var ONE_RADIAN = 180 / Math.PI;
var ONE_DEGREES = Math.PI / 180;
var TWO_PI = Math.PI * 2;
var HALF_PI = Math.PI / 2;

function drawLine(ctx, x0, y0, x1, y1, style) {
   ctx.moveTo(x0, y0);
   ctx.lineTo(x1, y1);
   if (style) {
      ctx.strokeStyle = style;
   }
   ctx.stroke();
}

/*
 function drawEllipse(){
 ctx.beginPath();
 ctx.arc(95,50,40,0,2*Math.PI);
 ctx.stroke();
 }
 */
function drawText(ctx, text, font, size, weight, fontStyle, x0, y0, style, align, color) {
   var f = "";
   if (weight) {
      f += weight + " ";
   }
   if (fontStyle) {
      f += fontStyle + " ";
   }
   f += size + "px " + font;
   ctx.font = f;
   if (align) {
      ctx.textAlign = align;
   }
   if (color) {
      ctx.fillStyle = color;
   }
   if (style === "stroke") {
      ctx.strokeText(text, x0, y0);
   } else {
      ctx.fillText(text, x0, y0);
   }
}

function drawImage(ctx, imgSrc, x0, y0) {
   // TODO: load image from source, not from id
   var img = document.getElementById(imgSrc);
   ctx.drawImage(img, x0, y0);
}

function drawRect(ctx, x0, y0, x1, y1, fill, border, shadow) {
   if (fill) {
      ctx.fillStyle = fill;
   }
   if (shadow) {
      ctx.shadowBlur = shadow.blur;
      ctx.shadowColor = shadow.color;
      ctx.shadowOffsetX = shadow.offx;
      ctx.shadowOffsetY = shadow.offy;
   }
   ctx.fillRect(x0, y0, x1, y1);
   if (shadow) {
      ctx.shadowBlur = 0;
      ctx.shadowOffsetX = 0;
      ctx.shadowOffsetY = 0;
   }
   if (border) {
      ctx.strokeStyle = border;
      ctx.rect(x0, y0, x1, y1);
      ctx.stroke();
   }
}

function drawArc(ctx, cx, cy, ray, begin, end, fill, border, shadow) {
   if (fill) {
      ctx.fillStyle = fill;
   }
   if (shadow) {
      ctx.shadowBlur = shadow.blur;
      ctx.shadowColor = shadow.color;
      ctx.shadowOffsetX = shadow.offx;
      ctx.shadowOffsetY = shadow.offy;
   }
   ctx.beginPath();
   ctx.arc(cx, cy, ray, begin, end);
   ctx.fill();
   if (shadow) {
      ctx.shadowBlur = 0;
      ctx.shadowOffsetX = 0;
      ctx.shadowOffsetY = 0;
   }
   if (border) {
      ctx.strokeStyle = border;
      ctx.beginPath();
      ctx.arc(cx, cy, ray, begin, end);
      ctx.stroke();
   }
}

function edgeOfView(rect, deg) {
   var twoPI = Math.PI * 2;
   var theta = deg * Math.PI / 180;

   while (theta < -Math.PI) {
      theta += twoPI;
   }

   while (theta > Math.PI) {
      theta -= twoPI;
   }

   var rectAtan = Math.atan2(rect.height, rect.width);
   var tanTheta = Math.tan(theta);
   var region;

   if ((theta > -rectAtan) && (theta <= rectAtan)) {
      region = 1;
   } else if ((theta > rectAtan) && (theta <= (Math.PI - rectAtan))) {
      region = 2;
   } else if ((theta > (Math.PI - rectAtan)) || (theta <= -(Math.PI - rectAtan))) {
      region = 3;
   } else {
      region = 4;
   }

   var edgePoint = {x: rect.x0 + rect.width / 2, y: rect.y0 + rect.height / 2};
   var xFactor = 1;
   var yFactor = 1;

   switch (region) {
      case 1:
	 yFactor = -1;
	 break;
      case 2:
	 yFactor = -1;
	 break;
      case 3:
	 xFactor = -1;
	 break;
      case 4:
	 xFactor = -1;
	 break;
   }

   if ((region === 1) || (region === 3)) {
      edgePoint.x += xFactor * (rect.width / 2.);
      edgePoint.y += yFactor * (rect.width / 2.) * tanTheta;
   } else {
      edgePoint.x += xFactor * (rect.height / (2. * tanTheta));
      edgePoint.y += yFactor * (rect.height / 2.);
   }

   return edgePoint;
}

function drawLink(ctx, frameA, frameB) {
   var cax = frameA.x0 + frameA.width / 2;
   var cay = frameA.y0 + frameA.height / 2;
   console.log("a.c[" + cax + "," + cay + "]");
   var cbx = frameB.x0 + frameB.width / 2;
   var cby = frameB.y0 + frameB.height / 2;
   console.log("a.c[" + cbx + "," + cby + "]");
   var dx = Math.abs(cbx - cax);
   var dy = Math.abs(cby - cay);
   console.log("d[" + dx + "," + dy + "]");

   var at = dx / dy;
   var ar = Math.atan(at);
   var r;
   if (cax >= cbx) {
      if (cay >= cby) {
	 ar = (Math.PI * 1.5 - ar);
	 r = 1;
      } else {
	 ar = (Math.PI * 0.5 + ar);
	 r = 3;
      }
   } else {
      if (cay > cby) {
	 ar = (ar - Math.PI * 0.5);
	 r = 4;
      } else {
	 ar = (Math.PI * 0.5 - ar);
	 r = 2;
      }
   }
   var ad = ar * ONE_RADIAN;

   var pointA = edgeOfView(frameA, r > 2 ? 90 + ad : 360 - ad);
   //console.log("pA[x,y]=[" + pointA.x + "," + pointA.y + "]");
   var pointB = edgeOfView(frameB, r > 2 ? 270 + ad : 180 - ad);
   //console.log("pB[x,y]=[" + pointB.x + "," + pointB.y + "]");

   //drawLine(ctx, cax, cay, cax + Math.cos(ar) * frameA.width, cay + Math.sin(ar) * frameA.width);
   drawLine(ctx, pointA.x, pointA.y, pointB.x, pointB.y);
   var begin = 0;
   var end = Math.PI;
   if (r === 1 || r === 4) {
      begin = Math.PI;
      end = 2 * Math.PI;
   }
   drawArc(ctx, pointA.x, pointA.y, 5, begin, end);
   //console.log("at: " + at + " ar: " + ar + " ad:" + (ad));
   //console.log("p[x,y]=[" + pointA.x + "," + pointA.y + "]");
}

function createLinearGradient(ctx, colors, startx, stary, endx, endy) {
   var grd = ctx.createLinearGradient(startx, stary, endx, endy);
   for (c = 0; c < colors.length; c++) {
      grd.addColorStop(c / (colors.length - 1.0), colors[c]);
   }
   return grd;
}

function createShadowObject(blur, offx, offy, color) {
   return {"blur": blur, "offx": offx, "offy": offy, "color": color};
}
