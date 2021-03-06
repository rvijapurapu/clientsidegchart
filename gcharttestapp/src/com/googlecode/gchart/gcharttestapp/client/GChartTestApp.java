package com.googlecode.gchart.gcharttestapp.client;
//<<1
// Required imports:
import com.googlecode.gchart.client.GChartCanvasLite;
import com.googlecode.gchart.client.GChartCanvasFactory;
import com.googlecode.gchart.client.GChart;
import com.googlecode.gchart.client.GChart.LegendLocation;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas; 
import com.google.gwt.widgetideas.graphics.client.Color;
//>>1

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;



/** 
 * 
 * Displays the test  chart in the browser, and checks the HTML
 * generated against previous, visually validated, browser
 * output HTML hash codes to see if HTML output from the test
 * has changed (possibly due to an error).
 * 
 * As long as GChart and the test set itself has not changed,
 * these tests can be performed by first running in hosted
 * mode, then clicking on the "Compile/Browse" button. If
 * "Test passed" is displayed in hosted mode and also in
 * Firefox after the compile (this assumes Firefox is your default browser)
 * it means that the generated HTML has not changed since
 * the last time it was visually inspected--test passed. 
 * 
 * If the test or GChart changes so as to change browser output,
 * you will have to visually verify the charts, and then (assuming the 
 * new charts are correct) enter the new hashcodes.
 * 
 * In the most common case where the test and output are unchanged,
 * the test should go through very quickly.
 * 
 */
public class GChartTestApp implements EntryPoint {

//<<2
   
// Paste these lines into the top-level GWT class that kicks off your
// application (the one that "implements EntryPoint"):
   static {
      final class GWTCanvasBasedCanvasLite
         extends GWTCanvas implements GChartCanvasLite {
     // GChartCanvasLite requires CSS/RGBA color strings, but
     // GWTCanvas uses its own Color class instead, so we wrap:
         public void setStrokeStyle(String cssColor) {
            setStrokeStyle(new Color(cssColor));
         }
         public void setFillStyle(String cssColor) {
            setFillStyle(new Color(cssColor));
         }
      // Note: all other GChartCanvasLite methods (lineTo, moveTo,
      // arc, etc.) are directly inherited from GWTCanvas, so no
      // wrapper methods are needed.
      }
      final class GWTCanvasBasedCanvasFactory 
          implements GChartCanvasFactory {
         public GChartCanvasLite create() {
           GChartCanvasLite result = new GWTCanvasBasedCanvasLite();
           return result;
         }
      }
   
    // This line "teaches" GChart how to create the canvas
    // widgets it needs to render any continuous,
    // non-rectangular, chart aspects (solid fill pie slices,
    // continously connected lines, etc.) clearly and
    // efficiently.  It's generally best to do this exactly once,
    // when your entire GWT application loads.
      GChart.setCanvasFactory(new GWTCanvasBasedCanvasFactory());
   }
//>>2
   
  // convenience method to create a short, class-name-based title
  static String getTitle(Object obj) {
    String result = obj.getClass().getName();
    result = result.substring(result.lastIndexOf(".")+1);
    return "<h4><br>" + result + "</h4>";
  }

  /* Linear congruent random number generator.
   *
   * Cannot use GWT's Math.random() because, for automated
   * testing, we require that the exact same random sequence
   * be used each time (GWT does not support the JDK's more
   * generic Random class, which would have allowed this).
   *
   * Constants are from Knuth via Numerical Recipes in C.
   * 
   */ 
  static double i = 0;
  static double rnd() {
    final int m = 217728;             
    final int a = 84589;
    final int c = 45989;
    i = (a*i + c) % m; 
    return i/m;
  }
  
  /* 
   * Allows us to quickly check if test charts changed since last time
   * they were manually inspected, thus eliminating the many tedious
   * re-inspections I used to have to do. 
   * 
   * Whenever the test changes, or GChart changes in a way that changes
   * generated HTML, manual chart re-inspection is needed and 
   * the various hashcodes below need to be re-entered.
   * 
   * Note that sometimes the hosted mode cold-start hash code is generated by
   * a refresh, so if you get the same hash code the first time
   * you press refresh, try again to produce the second, refresh, hash code.
   * 
   * Note, that if you open the compiled app directly from the file system
   * rather than via the Compile/Browse button, Firefox produces a different
   * HTML/hash code, so you must run the test via the Compile/Browse button.
   * 
   */
   
//  private static void checkHashCode() {
// // The hosted-mode HTML varies depending on browser, other factors. These
// // are the hash codes whose charts have been visually verified as OK.	 
// // (add to this list as new browsers, etc. are visually certified. List
////	 must be regenerated whenever test set charts change).	  
//       final HashSet<String> hashCodes = new HashSet<String>();
//       hashCodes.add("-69786332"); // hosted mode browser
//// add more verified hash codes here (e.g. one for each browser tested)
//       String hashCode = "" + DOM.getInnerHTML(RootPanel.getBodyElement()).hashCode();
//       
//       if (!hashCodes.contains(hashCode)) {
//          System.out.println("***Hashcode changed!! New hashcode="+hashCode + " Time=" + new Date());
//          Window.alert("***Hash code changed!! New hashcode = " + hashCode);
//       }
//       else  {
//          System.out.println("Previously verified hashcode recognized. Hashcode=" + hashCode);
//          Window.alert("Previously verified hashcode recognized. Hashcode=" + hashCode);
//       }
//
//  }  


  
   static private class AddOneChart implements Command {
	 boolean needsUpdate = true;
     GChart gchart;       
     AddOneChart(GChart gchart, boolean needsUpdate) {
    	 this.gchart = gchart;
         this.needsUpdate = needsUpdate;	 
     }
     public void execute() {
	    RootPanel.get("testappcharts").add(new HTML(getTitle(gchart)));
       RootPanel.get("testappcharts").add(gchart);
       if (needsUpdate) gchart.update();
     }
   }

   
  static void addChart(GChart gchart) {
     DeferredCommand.addCommand(new AddOneChart(gchart, true));
  }
  static void addChartNoUpdate(GChart gchart) {
	 DeferredCommand.addCommand(new AddOneChart(gchart, false));
  }
	   
  public void onModuleLoad() {

// thinking about retiring these tests: 
//    addChart(new GChartExample11(0,2,false));
//    addChart(new GChartExample11(0,3,false));
//    addChart(new GChartExample11(0,4,false));
//    addChart(new GChartExample11(0,5,false));
//    addChart(new GChartExample11(0,6,false));
//    addChart(new GChartExample11(0,7,false));
//    addChart(new GChartExample11(0,8,false));
//    addChart(new TestGChart40());
//    DeferredCommand.addCommand(new Command() { public void execute() {
//    RootPanel.get().add(new TestGChart41a());
//  }});

// To focus on a single test, simply use Eclipse's Source, Toggle comment        
    DeferredCommand.addCommand(new Command() { public void execute() {
    addChart(new GChartExample00());
    addChart(new GChartExample00a());
    addChart(new GChartExample00b());
    addChart(new GChartExample00c());
    addChart(new GChartExample01());
    addChart(new GChartExample01a(3));
    addChart(new GChartExample01a(0));
    addChart(new GChartExample01b());
    addChart(new GChartExample01c());
    addChart(new GChartExample02());
    addChart(new GChartExample03());
    addChart(new GChartExample04());
    addChart(new GChartExample04a());
    addChart(new GChartExample04b());
    addChart(new GChartExample05());
    addChart(new GChartExample06());
    addChart(new GChartExample07());
    addChart(new GChartExample08());
    addChart(new GChartExample09());
    addChart(new GChartExample10());
    addChart(new GChartExample11(0,1,true));
    addChart(new GChartExample11(0,1,false));
    addChart(new GChartExample11(1,1,false));
    addChart(new GChartExample11(2,1,false));
    addChart(new GChartExample11(3,1,false));
    addChart(new GChartExample12());
    addChart(new GChartExample14());
    addChart(new GChartExample15());
    addChart(new GChartExample15a());
    addChart(new GChartExample15b());
    addChart(new GChartExample16());
      addChart(new GChartExample17());
      addChart(new GChartExample17a());
      addChart(new GChartExample18());
      addChart(new GChartExample18a());
      addChart(new GChartExample19());
        addChart(new GChartExample20());
      addChart(new GChartExample20a());
      addChart(new GChartExample21());
      addChart(new GChartExample22());
      addChart(new GChartExample22a());
      addChart(new GChartExample23(false, false));
      addChart(new GChartExample23(false, true));
      addChart(new GChartExample23(true, false));
      addChart(new GChartExample23(true, true));
      addChart(new GChartExample24());
  DeferredCommand.addCommand(new Command() { public void execute() {
    RootPanel.get().add(new GChartExample25());
  }});
    addChart(new TestGChart00());
    addChartNoUpdate(new TestGChart01(0,0));
    addChartNoUpdate(new TestGChart01(1,0));
    addChartNoUpdate(new TestGChart01(2,0));
    addChartNoUpdate(new TestGChart01(3,0));
    addChartNoUpdate(new TestGChart01(0,1));
    addChartNoUpdate(new TestGChart01(1,1));
    addChartNoUpdate(new TestGChart01(2,1));
    addChartNoUpdate(new TestGChart01(3,1));
    addChartNoUpdate(new TestGChart01(0,2));
    addChartNoUpdate(new TestGChart01(1,2));
    addChartNoUpdate(new TestGChart01(2,2));
    addChartNoUpdate(new TestGChart01(3,2));
    addChart(new TestGChart01a());
    addChart(new TestGChart02());
    addChart(new TestGChart03());
    addChart(new TestGChart04());
    addChart(new TestGChart04a());
    addChart(new TestGChart05(false));
    addChart(new TestGChart05(true));
    addChart(new TestGChart06(false));
    addChart(new TestGChart06(true));
    addChart(new TestGChart07(0,0,10,1));
    addChart(new TestGChart07(10,0,10,1));
    addChart(new TestGChart07(-10,0,10,1));
    addChart(new TestGChart07(0,10,10,1));
    addChart(new TestGChart07(0,-10,10,1));
    addChart(new TestGChart07(0,0,30,1));
    addChart(new TestGChart07(0,0,10,3));
    addChart(new TestGChart07(0,0,10,-1));
    addChart(new TestGChart07(0,0,10,-2));
    addChart(new TestGChart07(0,0,10,-3));
    addChart(new TestGChart07a());
    addChart(new TestGChart08());
    addChart(new TestGChart09());
    addChart(new TestGChart10());
    addChart(new TestGChart11());
    addChart(new TestGChart12());
    addChart(new TestGChart14());
    addChart(new TestGChart14a());
    addChart(new TestGChart14b());
    addChart(new TestGChart14c());
    addChart(new TestGChart14d());
    addChart(new TestGChart15(1));
    addChart(new TestGChart15(0));
    addChart(new TestGChart16(0,1));
    addChart(new TestGChart16(1,1));
    addChart(new TestGChart16(2,1));
    addChart(new TestGChart16(3,1));
    addChart(new TestGChart16(4,1));
    addChart(new TestGChart16(5,1));
    addChart(new TestGChart16(0,10));
    addChart(new TestGChart16(1,10));
    addChart(new TestGChart16(2,10));
    addChart(new TestGChart16(3,10));
    addChart(new TestGChart16(4,10));
    addChart(new TestGChart16(5,10));
    addChart(new TestGChart17());
    addChart(new TestGChart17a());
    addChart(new TestGChart17b());
    addChart(new TestGChart18());
    addChart(new TestGChart19(0));
    addChart(new TestGChart19(1));
    addChart(new TestGChart19(2));
    addChart(new TestGChart19(3));
    addChart(new TestGChart20());
    addChart(new TestGChart20a());
    // extra layer to stop "this script is taking too long" browser msg   
    DeferredCommand.addCommand(new Command() { public void execute() {
    addChart(new TestGChart21());
    addChart(new TestGChart22(false));
    addChart(new TestGChart22(true));
    addChart(new TestGChart23(1));
    addChart(new TestGChart23(2));
    addChart(new TestGChart23(3));
    addChart(new TestGChart24(1));
    addChart(new TestGChart24(2));
    addChart(new TestGChart25(8,1,1,1,20,20));
    addChart(new TestGChart25(8,2,2,1,1,1));
    addChart(new TestGChart25(8,5,5,1,0,0));
    addChart(new TestGChart25(8,1,1,.5,10,10));
    addChart(new TestGChart25(8,2,2,.5,10,10));
    addChart(new TestGChart25(8,4,4,.5,10,10));
    addChart(new TestGChart25(8,8,8,.5,10,10));
    addChart(new TestGChart25(8,1,1,1.5,20,20));
    addChart(new TestGChart25b(8,1,0,1.5,20,20));
    addChart(new TestGChart25b(8,5,5,1,0,0));
    addChart(new TestGChart26());
    addChart(new TestGChart27(true));
    addChart(new TestGChart27(false));
    addChart(new TestGChart28(false, 1, false));
    addChart(new TestGChart28(true, 1, false));
    addChart(new TestGChart28(false, 2, false));
    addChart(new TestGChart28(true, 2, false));
    addChart(new TestGChart28(false, 4, false));
    addChart(new TestGChart28(true, 4, false));

    addChart(new TestGChart28(false, 1, true));
    addChart(new TestGChart28(true, 1, true));
    addChart(new TestGChart28(false, 2, true));
    addChart(new TestGChart28(true, 2, true));
    addChart(new TestGChart28(false, 4, true));
// Hover testing charts start here
    addChart(new TestGChart28(true, 4, true));    
    addChart(new TestGChart29());
    addChart(new TestGChart30(false, false));
    addChart(new TestGChart30(true, false));
    addChart(new TestGChart30(false, true));
    addChart(new TestGChart30(true, true));
    addChart(new TestGChart31());
    addChart(new TestGChart32());
// Not a GCchart, a ScrollPanel that contains a Gchart:    	
    DeferredCommand.addCommand(new Command() { public void execute() {
      RootPanel.get().add(new TestGChart33());
    }});
     addChart(new TestGChart34());
     addChart(new TestGChart35());
      addChart(new  TestGChart36(GChart.TouchedPointUpdateOption.TOUCHED_POINT_CLEARED));
      addChart(new TestGChart36(GChart.TouchedPointUpdateOption.TOUCHED_POINT_LOCKED));
      addChart(new TestGChart36(GChart.TouchedPointUpdateOption.TOUCHED_POINT_UPDATED));
        addChart(new TestGChart37(GChart.TouchedPointUpdateOption.TOUCHED_POINT_CLEARED));
        addChart(new TestGChart37(GChart.TouchedPointUpdateOption.TOUCHED_POINT_LOCKED));
        addChart(new TestGChart37(GChart.TouchedPointUpdateOption.TOUCHED_POINT_UPDATED));
        addChart(new TestGChart38(GChart.TouchedPointUpdateOption.TOUCHED_POINT_CLEARED));
        addChart(new TestGChart38(GChart.TouchedPointUpdateOption.TOUCHED_POINT_LOCKED));
        addChart(new TestGChart38(GChart.TouchedPointUpdateOption.TOUCHED_POINT_UPDATED));
        addChart(new TestGChart39());
        DeferredCommand.addCommand(new Command() { public void execute() {
            RootPanel.get().add(new TestGChart41());
        }});
        DeferredCommand.addCommand(new Command() { public void execute() {
            RootPanel.get().add(new TestGChart41a());
        }});
      addChart(new TestGChart42());
      addChart(new TestGChart43());
      addChart(new TestGChart44());
        addChart(new TestGChart45(0));
      addChart(new TestGChart45(1));
    	addChart(new TestGChart45(2));
      addChart(new TestGChart45(3));
      addChart(new TestGChart45(4));
      DeferredCommand.addCommand(new Command() { public void execute() {
      RootPanel.get().add(new TestGChart46());
  }});
      addChart(new TestGChart47(0,1,5));
      addChart(new TestGChart47(1,1,5));
      addChart(new TestGChart47(2,1,5));
      addChart(new TestGChart47(3,1,5));
      addChart(new TestGChart47(4,1,5));
      addChart(new TestGChart47(5,1,5));
      addChart(new TestGChart47(6,1,5));
      addChart(new TestGChart47(7,1,5));
      addChart(new TestGChart47(0,0,2));
      addChart(new TestGChart47(1,0,2));
      addChart(new TestGChart47(2,0,2));
      addChart(new TestGChart47(3,0,2));
      addChart(new TestGChart47(4,0,2));
      addChart(new TestGChart47(5,0,2));
      addChart(new TestGChart47(6,0,2));
      addChart(new TestGChart47(7,0,2));
      addChart(new TestGChart47(0,1,3));
      addChart(new TestGChart47(1,1,3));
      addChart(new TestGChart47(2,1,3));
      addChart(new TestGChart47(3,1,3));
      addChart(new TestGChart47(4,1,3));
      addChart(new TestGChart47(5,1,3));
      addChart(new TestGChart47(6,1,3));
      addChart(new TestGChart47(7,1,3));
      addChart(new TestGChart47(8,3,5));
      addChart(new TestGChart47(9,3,5));
      addChart(new TestGChart48(3));
      addChart(new TestGChart48(4));
      addChart(new TestGChart48(5));
      addChart(new TestGChart48(6));
      addChart(new TestGChart48(8));
      addChart(new TestGChart48(9));
      addChart(new TestGChart48(10));
      addChart(new TestGChart49(3,false, false));
      addChart(new TestGChart49(3, false, true));
      addChart(new TestGChart49(3,true, false));
      addChart(new TestGChart49(3, true, true));
      addChart(new TestGChart50(GChart.TickLocation.CENTERED,0));
      addChart(new TestGChart50(GChart.TickLocation.INSIDE,0));
      addChart(new TestGChart50(GChart.TickLocation.OUTSIDE,0));
      addChart(new TestGChart50(GChart.TickLocation.CENTERED,5));
      addChart(new TestGChart50(GChart.TickLocation.INSIDE,5));
      addChart(new TestGChart50(GChart.TickLocation.OUTSIDE,5));
      addChart(new TestGChart51(0));
      addChart(new TestGChart51(1));
      addChart(new TestGChart52());
      addChart(new TestGChart53());
      addChart(new TestGChart54());
      DeferredCommand.addCommand(new Command() { public void execute() {
          RootPanel.get().add(new TestGChart55());
      }});
      DeferredCommand.addCommand(new Command() { public void execute() {
          RootPanel.get().add(new TestGChart55a());
      }});
      addChart(new TestGChart56());

      addChart(new TestGChart57());
      DeferredCommand.addCommand(new Command() { public void execute() {
  	    RootPanel.get().add(new TestGChart57a());
      }});
      DeferredCommand.addCommand(new Command() { public void execute() {
  	    RootPanel.get().add(new TestGChart58());
      }});
      addChart(new TestGChart59(LegendLocation.OUTSIDE_LEFT,0,0, null));
      addChart(new TestGChart59(LegendLocation.OUTSIDE_RIGHT,0,0, null));
      addChart(new TestGChart59(LegendLocation.INSIDE_LEFT,0,0, null));
      addChart(new TestGChart59(LegendLocation.INSIDE_RIGHT,0,0, null));
      addChart(new TestGChart59(LegendLocation.INSIDE_TOP,0,0, null));
      addChart(new TestGChart59(LegendLocation.INSIDE_TOPLEFT,5,-5, null));
      addChart(new TestGChart59(LegendLocation.INSIDE_TOPRIGHT,-5,-5, null));
      addChart(new TestGChart59(LegendLocation.INSIDE_BOTTOM,0,0, null));
      addChart(new TestGChart59(LegendLocation.INSIDE_BOTTOMLEFT,0,0, null));
      addChart(new TestGChart59(LegendLocation.INSIDE_BOTTOMRIGHT,-10,10, null));
        
      addChart(new TestGChart59(LegendLocation.OUTSIDE_LEFT,0,0, new HTML("x<br>y<br>z")));
      addChart(new TestGChart59(LegendLocation.OUTSIDE_RIGHT,0,0, new HTML("x<br>y<br>z")));
      addChart(new TestGChart59(LegendLocation.INSIDE_LEFT,0,0, new HTML("x<br>y<br>z")));
      addChart(new TestGChart59(LegendLocation.INSIDE_RIGHT,0,0, new HTML("x<br>y<br>z")));
      addChart(new TestGChart59(LegendLocation.INSIDE_TOP,0,0, new HTML("<table><tr><td>x <td>y <td>z</tr></table>")));
      addChart(new TestGChart59(LegendLocation.INSIDE_TOPLEFT,5,-5, new HTML("x<br>y<br>z")));
      addChart(new TestGChart59(LegendLocation.INSIDE_TOPRIGHT,-5,-5, new HTML("x<br>y<br>z")));
      addChart(new TestGChart59(LegendLocation.INSIDE_BOTTOM,0,0, new HTML("<table><tr><td>x <td>y <td>z</tr></table>")));
      addChart(new TestGChart59(LegendLocation.INSIDE_BOTTOMLEFT,0,0, new HTML("x<br>y<br>z")));
      addChart(new TestGChart59(LegendLocation.INSIDE_BOTTOMRIGHT,-10,10, new HTML("x<br>y<br>z")));
      
    }});      

// These tests explored a (rejected) ASCII-art rendering idea    
//    addChart(new TestGChart60());
//    addChart(new TestGChart61());
 	  
    RootPanel.get("loadingMessage").setVisible(false); 
    
    }});
    
  }

}  