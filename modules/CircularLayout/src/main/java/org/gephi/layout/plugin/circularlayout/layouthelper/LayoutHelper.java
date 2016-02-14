/*
 Copyright (c) 2010, Matt Groeninger
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, are
 permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this list of
 conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice, this list
 of conditions and the following disclaimer in the documentation and/or other materials
 provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.gephi.layout.plugin.circularlayout.layouthelper;

import java.util.*;
import org.gephi.graph.api.*;
import org.gephi.layout.spi.Layout;
import org.gephi.layout.spi.LayoutBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Matt
 */
public abstract class LayoutHelper implements Layout {
      private final LayoutBuilder layoutBuilder;
      protected GraphModel graphModel;
      private boolean converged;
      private CircularDirection NodePlacementDirection = CircularDirection.CW;

      public static enum CircularDirection {
          CCW,
          CW
      }

  @NbBundle.Messages({
    "Layout_name=Helper Class",
    "Layout_NodePlacement_Random_name=Random",
    "Layout_NodePlacement_NodeID_name=Node ID",
    "Layout_NodePlacement_Degree_name=Degree",
    "Layout_NodePlacement_InDegree_name=In Degree",
    "Layout_NodePlacement_OutDegree_name=Out Degree",
    "Layout_NodePlacement_Mutual_name=Mutual Degree",
    "Layout_NodePlacement_CCW=Counter Clockwise",
    "Layout_NodePlacement_CW=Clockwise"
  })

   public LayoutHelper(LayoutBuilder layoutBuilder) {
       this.layoutBuilder = layoutBuilder;
   }

   @Override
   public LayoutBuilder getBuilder() {
       return layoutBuilder;
   }

   @Override
   public void setGraphModel(GraphModel graphModel) {
       this.graphModel = graphModel;
   }

   @Override
   public boolean canAlgo() {
       return !isConverged() && graphModel != null;
   }

   public void setConverged(boolean converged) {
       this.converged = converged;
   }

   public boolean isConverged() {
       return converged;
   }

   public CircularDirection getNodePlacementDirection() {
       return this.NodePlacementDirection;
   }

   public void setNodePlacementDirection(CircularDirection NodePlacementDirection) {
       this.NodePlacementDirection = NodePlacementDirection;
   }

   public boolean isCW() {
     if (this.NodePlacementDirection == CircularDirection.CW) {
       return true;
     }
     return false;
   }

   public boolean isCCW() {
     if (this.NodePlacementDirection == CircularDirection.CCW) {
       return true;
     }
     return false;
   }

  public static Map getPlacementMap() {
      return getPlacementMap(true);
  }
  public static Map getPlacementMap(boolean IncludeRandom) {
      GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
      GraphModel objGraphModel = graphController.getGraphModel();
      Map<String, String> map = new TreeMap<String, String>();
      if (IncludeRandom) {
        map.put("Random", Bundle.Layout_NodePlacement_Random_name());
      }
      map.put("NodeID", Bundle.Layout_NodePlacement_NodeID_name());
      map.put("Degree", Bundle.Layout_NodePlacement_Degree_name());
      if (objGraphModel.isDirected()) {
          map.put("InDegree", Bundle.Layout_NodePlacement_InDegree_name());
          map.put("OutDegree", Bundle.Layout_NodePlacement_OutDegree_name());
          map.put("MutualDegree", Bundle.Layout_NodePlacement_Mutual_name());
      }
      for (Column c : objGraphModel.getNodeTable()) {
          map.put(c.getId() + "-Att", c.getTitle() + " (Attribute)");
      }
      return map;
  }

  public static Map getRotationMap() {
      EnumMap<CircularDirection, String> map = new EnumMap<CircularDirection, String>(CircularDirection.class);
      map.put(CircularDirection.CCW, Bundle.Layout_NodePlacement_CCW());
      map.put(CircularDirection.CW, Bundle.Layout_NodePlacement_CW());
      return map;
  }

  public float[] cartCoors(double radius, int whichInt, double theta) {
      float[] coOrds = new float[2];
      coOrds[0] = (float) (radius * (Math.cos((theta * whichInt) + (Math.PI / 2))));
      coOrds[1] = (float) (radius * (Math.sin((theta * whichInt) + (Math.PI / 2))));
      return coOrds;
  }
}
