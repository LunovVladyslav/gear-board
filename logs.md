--------- beginning of crash
--------- beginning of system
2026-03-29 14:16:40.221 23993-23993 AndroidRuntime          pid-23993                            E  FATAL EXCEPTION: main
Process: com.gearboard, PID: 23993
java.lang.IllegalArgumentException: LazyVerticalGrid's width should be bound by parent.
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke-0kLqBqw(LazyGridDsl.kt:155)
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke(LazyGridDsl.kt:154)
at androidx.compose.foundation.lazy.grid.GridSlotCache.invoke-0kLqBqw(LazyGridDsl.kt:235)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke-0kLqBqw(LazyGrid.kt:216)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke(LazyGrid.kt:177)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.SizeNode.measure-3p2s80s(Size.kt:838)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.ScrollingLayoutNode.measure-3p2s80s(Scroll.kt:394)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
2026-03-29 14:18:02.985 24596-24596 AndroidRuntime          pid-24596                            E  FATAL EXCEPTION: main
Process: com.gearboard, PID: 24596
java.lang.IllegalArgumentException: LazyVerticalGrid's width should be bound by parent.
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke-0kLqBqw(LazyGridDsl.kt:155)
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke(LazyGridDsl.kt:154)
at androidx.compose.foundation.lazy.grid.GridSlotCache.invoke-0kLqBqw(LazyGridDsl.kt:235)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke-0kLqBqw(LazyGrid.kt:216)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke(LazyGrid.kt:177)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.SizeNode.measure-3p2s80s(Size.kt:838)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.ScrollingLayoutNode.measure-3p2s80s(Scroll.kt:394)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
2026-03-29 14:59:50.259  5988-5988  AndroidRuntime          pid-5988                             E  FATAL EXCEPTION: main
Process: com.gearboard, PID: 5988
java.lang.IllegalArgumentException: LazyVerticalGrid's width should be bound by parent.
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke-0kLqBqw(LazyGridDsl.kt:155)
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke(LazyGridDsl.kt:154)
at androidx.compose.foundation.lazy.grid.GridSlotCache.invoke-0kLqBqw(LazyGridDsl.kt:235)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke-0kLqBqw(LazyGrid.kt:216)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke(LazyGrid.kt:177)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.SizeNode.measure-3p2s80s(Size.kt:838)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.ScrollingLayoutNode.measure-3p2s80s(Scroll.kt:394)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
2026-03-29 14:59:57.517  6113-6113  AndroidRuntime          pid-6113                             E  FATAL EXCEPTION: main
Process: com.gearboard, PID: 6113
java.lang.IllegalArgumentException: LazyVerticalGrid's width should be bound by parent.
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke-0kLqBqw(LazyGridDsl.kt:155)
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke(LazyGridDsl.kt:154)
at androidx.compose.foundation.lazy.grid.GridSlotCache.invoke-0kLqBqw(LazyGridDsl.kt:235)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke-0kLqBqw(LazyGrid.kt:216)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke(LazyGrid.kt:177)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.SizeNode.measure-3p2s80s(Size.kt:838)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.ScrollingLayoutNode.measure-3p2s80s(Scroll.kt:394)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
2026-03-29 15:00:08.220  6193-6193  AndroidRuntime          pid-6193                             E  FATAL EXCEPTION: main
Process: com.gearboard, PID: 6193
java.lang.IllegalArgumentException: LazyVerticalGrid's width should be bound by parent.
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke-0kLqBqw(LazyGridDsl.kt:155)
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke(LazyGridDsl.kt:154)
at androidx.compose.foundation.lazy.grid.GridSlotCache.invoke-0kLqBqw(LazyGridDsl.kt:235)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke-0kLqBqw(LazyGrid.kt:216)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke(LazyGrid.kt:177)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.SizeNode.measure-3p2s80s(Size.kt:838)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.ScrollingLayoutNode.measure-3p2s80s(Scroll.kt:394)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
2026-03-29 15:00:45.206  6292-6292  AndroidRuntime          pid-6292                             E  FATAL EXCEPTION: main
Process: com.gearboard, PID: 6292
java.lang.IllegalArgumentException: LazyVerticalGrid's width should be bound by parent.
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke-0kLqBqw(LazyGridDsl.kt:155)
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke(LazyGridDsl.kt:154)
at androidx.compose.foundation.lazy.grid.GridSlotCache.invoke-0kLqBqw(LazyGridDsl.kt:235)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke-0kLqBqw(LazyGrid.kt:216)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke(LazyGrid.kt:177)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.SizeNode.measure-3p2s80s(Size.kt:838)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.ScrollingLayoutNode.measure-3p2s80s(Scroll.kt:394)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
2026-03-29 15:00:56.635  6412-6412  AndroidRuntime          pid-6412                             E  FATAL EXCEPTION: main
Process: com.gearboard, PID: 6412
java.lang.IllegalArgumentException: LazyVerticalGrid's width should be bound by parent.
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke-0kLqBqw(LazyGridDsl.kt:155)
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke(LazyGridDsl.kt:154)
at androidx.compose.foundation.lazy.grid.GridSlotCache.invoke-0kLqBqw(LazyGridDsl.kt:235)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke-0kLqBqw(LazyGrid.kt:216)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke(LazyGrid.kt:177)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.SizeNode.measure-3p2s80s(Size.kt:838)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.ScrollingLayoutNode.measure-3p2s80s(Scroll.kt:394)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
--------- beginning of main
2026-03-29 15:31:00.090  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8208   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 15:31:00.135  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8208   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 15:31:00.172  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8208   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 15:31:00.187  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8208   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 15:38:16.951  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8208   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 15:38:16.954  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8208   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 15:57:53.031  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8208   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 16:00:49.420   949-1635  BufferQueueDebug        surfaceflinger                       E  [ActivityRecord{d08ac44 u0 com.gearboard/.MainActivity t8209}#24844](this:0xb4000073d6bd1a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'ActivityRecord{d08ac44 u0 com.gearboard/.MainActivity t8209}#24844'
2026-03-29 16:00:49.431   949-1637  BufferQueueDebug        surfaceflinger                       E  [889c6ae Splash Screen com.gearboard#24845](this:0xb4000073d6940a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from '889c6ae Splash Screen com.gearboard#24845'
2026-03-29 16:00:49.438   949-1637  BufferQueueDebug        surfaceflinger                       E  [Splash Screen com.gearboard#24846](this:0xb4000073d7acda80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'Splash Screen com.gearboard#24846'
2026-03-29 16:00:49.445  3374-3374  Zygote                  com.gearboard                        E  process_name_ptr:3374 com.gearboard
2026-03-29 16:00:49.450  3374-3374  com.gearboard           com.gearboard                        I  Late-enabling -Xcheck:jni
2026-03-29 16:00:49.458  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8209   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=268435456 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 16:00:49.474   949-1635  BufferQueueDebug        surfaceflinger                       E  [17f32d6 ActivityRecordInputSink com.gearboard/.MainActivity#24852](this:0xb4000073d6ba0a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from '17f32d6 ActivityRecordInputSink com.gearboard/.MainActivity#24852'
2026-03-29 16:00:49.475  3374-3374  com.gearboard           com.gearboard                        I  Using CollectorTypeCC GC.
2026-03-29 16:00:49.485  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8209   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=268435456 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 16:00:49.493  3374-3374  nativeloader            com.gearboard                        D  Load libframework-connectivity-tiramisu-jni.so using APEX ns com_android_tethering for caller /apex/com.android.tethering/javalib/framework-connectivity-t.jar: ok
2026-03-29 16:00:49.497  3374-3374  MessageMonitor          com.gearboard                        I  Load libmiui_runtime
2026-03-29 16:00:49.508  3374-3374  HighMemoryManager       com.gearboard                        I  High memory monitor not enabled
2026-03-29 16:00:49.509  3374-3374  re-initialized>         com.gearboard                        W  type=1400 audit(0.0:465479): avc:  granted  { execute } for  path="/data/data/com.gearboard/code_cache/startup_agents/a82ed9e3-agent.so" dev="dm-52" ino=1234699 scontext=u:r:untrusted_app:s0:c194,c257,c512,c768 tcontext=u:object_r:app_data_file:s0:c194,c257,c512,c768 tclass=file app=com.gearboard
2026-03-29 16:00:49.513  3374-3403  AppScoutStateMachine    com.gearboard                        D  3374-ScoutStateMachinecreated
2026-03-29 16:00:49.513  3374-3374  nativeloader            com.gearboard                        D  Load /data/user/0/com.gearboard/code_cache/startup_agents/a82ed9e3-agent.so using system ns (caller=<unknown>): ok
2026-03-29 16:00:49.517  3374-3374  com.gearboard           com.gearboard                        W  hiddenapi: DexFile /data/data/com.gearboard/code_cache/.studio/instruments-2fd5708c.jar is in boot class path but is not in a known location
2026-03-29 16:00:49.540  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8209   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=268435456 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 16:00:49.546  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8209   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=268435456 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 16:00:49.651  3374-3374  com.gearboard           com.gearboard                        W  Redefining intrinsic method java.lang.Thread java.lang.Thread.currentThread(). This may cause the unexpected use of the original definition of java.lang.Thread java.lang.Thread.currentThread()in methods that have already been compiled.
2026-03-29 16:00:49.651  3374-3374  com.gearboard           com.gearboard                        W  Redefining intrinsic method boolean java.lang.Thread.interrupted(). This may cause the unexpected use of the original definition of boolean java.lang.Thread.interrupted()in methods that have already been compiled.
2026-03-29 16:00:49.657  3374-3374  FileUtils               com.gearboard                        E  err open mi_exception_log errno=2
2026-03-29 16:00:49.657  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:49.658  3374-3374  Compatibil...geReporter com.gearboard                        D  Compat change id reported: 171979766; UID 10450; state: ENABLED
2026-03-29 16:00:49.658  3374-3374  Compatibil...geReporter com.gearboard                        D  Compat change id reported: 242716250; UID 10450; state: ENABLED
2026-03-29 16:00:49.869  3374-3374  nativeloader            com.gearboard                        D  Configuring clns-4 for other apk /data/app/~~LCLY2zNU0lRw0KkoCMVD2w==/com.gearboard-ukJRrf-v1ZfZwnwt6bTpQQ==/base.apk. target_sdk_version=34, uses_libraries=, library_path=/data/app/~~LCLY2zNU0lRw0KkoCMVD2w==/com.gearboard-ukJRrf-v1ZfZwnwt6bTpQQ==/lib/arm64, permitted_path=/data:/mnt/expand:/data/user/0/com.gearboard
2026-03-29 16:00:49.878  3374-3374  nativeloader            com.gearboard                        D  Load libframework-connectivity-jni.so using APEX ns com_android_tethering for caller /apex/com.android.tethering/javalib/framework-connectivity.jar: ok
2026-03-29 16:00:49.880  3374-3374  GraphicsEnvironment     com.gearboard                        V  Currently set values for:
2026-03-29 16:00:49.880  3374-3374  GraphicsEnvironment     com.gearboard                        V    angle_gl_driver_selection_pkgs=[]
2026-03-29 16:00:49.880  3374-3374  GraphicsEnvironment     com.gearboard                        V    angle_gl_driver_selection_values=[]
2026-03-29 16:00:49.881  3374-3374  GraphicsEnvironment     com.gearboard                        V  ANGLE GameManagerService for com.gearboard: false
2026-03-29 16:00:49.881  3374-3374  GraphicsEnvironment     com.gearboard                        V  com.gearboard is not listed in per-application setting
2026-03-29 16:00:49.881  3374-3374  GraphicsEnvironment     com.gearboard                        V  App is not on the allowlist for updatable production driver.
2026-03-29 16:00:49.884  3374-3374  ForceDarkHelperStubImpl com.gearboard                        I  initialize for com.gearboard , ForceDarkAppConfig: null
2026-03-29 16:00:49.885  3374-3374  nativeloader            com.gearboard                        D  Load libforcedarkimpl.so using system ns (caller=/system_ext/framework/miui-framework.jar): ok
2026-03-29 16:00:49.885  3374-3374  OpenGLRenderer          com.gearboard                        D  JNI_OnLoad success
2026-03-29 16:00:49.885  3374-3374  MiuiForceDarkConfig     com.gearboard                        I  setConfig density:2.750000, mainRule:0, secondaryRule:0, tertiaryRule:0
2026-03-29 16:00:49.891  3374-3374  Compatibil...geReporter com.gearboard                        D  Compat change id reported: 183155436; UID 10450; state: ENABLED
2026-03-29 16:00:49.909  3374-3374  MbrainDebugManagerImpl  com.gearboard                        D  getService failed
2026-03-29 16:00:49.920  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:49.928  3374-3430  libc                    com.gearboard                        W  Access denied finding property "vendor.migl.debug"
2026-03-29 16:00:49.928  3374-3430  libMiGL                 com.gearboard                        I  EnableDR: 0
2026-03-29 16:00:49.928  3374-3430  libMEOW                 com.gearboard                        D  meow new tls: 0xb4000073a6894a00
2026-03-29 16:00:49.928  3374-3430  libMEOW                 com.gearboard                        D  meow reload base cfg path: na
2026-03-29 16:00:49.928  3374-3430  libMEOW                 com.gearboard                        D  meow reload overlay cfg path: na
2026-03-29 16:00:49.928  3374-3430  QT                      com.gearboard                        W  qt_process_init() called
2026-03-29 16:00:49.928  3374-3430  QT                      com.gearboard                        E  [QT]file does not exist
2026-03-29 16:00:49.928  3374-3430  QT                      com.gearboard                        W  Support!!
2026-03-29 16:00:49.929  3374-3430  QT                      com.gearboard                        E  [QT]file does not exist
2026-03-29 16:00:49.929  3374-3430  libMEOW                 com.gearboard                        D  applied 1 plugins for [com.gearboard]:
2026-03-29 16:00:49.929  3374-3430  libMEOW                 com.gearboard                        D    plugin 1: [libMEOW_gift.so]: 0xb40000739af69480
2026-03-29 16:00:49.929  3374-3430  libMEOW                 com.gearboard                        D  rebuild call chain: 0xb4000073a68ae200
2026-03-29 16:00:49.929  3374-3430  libMEOW                 com.gearboard                        D  meow delete tls: 0xb4000073a6894a00
2026-03-29 16:00:49.979  3374-3374  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.df.effect.conflict"
2026-03-29 16:00:49.977  3374-3374  com.gearboard           com.gearboard                        W  type=1400 audit(0.0:465480): avc:  denied  { read } for  name="u:object_r:vendor_displayfeature_prop:s0" dev="tmpfs" ino=10046 scontext=u:r:untrusted_app:s0:c194,c257,c512,c768 tcontext=u:object_r:vendor_displayfeature_prop:s0 tclass=file permissive=0 app=com.gearboard
2026-03-29 16:00:50.067  3374-3374  M-ProMotion             com.gearboard                        I  M-ProMotion is disabled
2026-03-29 16:00:50.072  3374-3434  nativeloader            com.gearboard                        D  Load libpowerhalwrap_jni.so using system ns (caller=/system_ext/framework/mediatek-framework.jar): ok
2026-03-29 16:00:50.072  3374-3434  PowerHalWrapper         com.gearboard                        I  PowerHalWrapper.getInstance
2026-03-29 16:00:50.107  3374-3374  SurfaceFactory          com.gearboard                        I  [static] sSurfaceFactory = com.mediatek.view.impl.SurfaceFactoryImpl@2b3306f
2026-03-29 16:00:50.110  3374-3374  Compatibil...geReporter com.gearboard                        D  Compat change id reported: 237531167; UID 10450; state: DISABLED
2026-03-29 16:00:50.115  3374-3374  FramePredict            com.gearboard                        D  FramePredict init: false
2026-03-29 16:00:50.116  3374-3374  FramePredict            com.gearboard                        D  init success
2026-03-29 16:00:50.116  3374-3374  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.perf.scroll_opt"
2026-03-29 16:00:50.121  3374-3427  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.display.iris_x7.support"
2026-03-29 16:00:50.122  3374-3437  FramePredict            com.gearboard                        E  registerContentObserver fail
2026-03-29 16:00:50.124  3374-3374  ForceDarkHelperStubImpl com.gearboard                        I  setViewRootImplForceDark: false for com.gearboard.MainActivity@cd6a74e, reason: AppDarkModeEnable
2026-03-29 16:00:50.125  3374-3374  VRI[MainActivity]       com.gearboard                        D  hardware acceleration = true, forceHwAccelerated = false
2026-03-29 16:00:50.133   949-980   BufferQueueDebug        surfaceflinger                       E  [a8ea596 com.gearboard/com.gearboard.MainActivity#24853](this:0xb4000073d6911a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'a8ea596 com.gearboard/com.gearboard.MainActivity#24853'
2026-03-29 16:00:50.138  3374-3374  libMEOW                 com.gearboard                        D  meow new tls: 0xb40000733d7c1280
2026-03-29 16:00:50.138  3374-3374  libMEOW                 com.gearboard                        D  applied 1 plugins for [com.gearboard]:
2026-03-29 16:00:50.138  3374-3374  libMEOW                 com.gearboard                        D    plugin 1: [libMEOW_gift.so]: 0xb40000739af69480
2026-03-29 16:00:50.138  3374-3374  libMEOW                 com.gearboard                        D  rebuild call chain: 0xb40000733d7d0200
2026-03-29 16:00:50.140  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:50.140  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:50.142  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:50.330  3374-3374  Compatibil...geReporter com.gearboard                        D  Compat change id reported: 263076149; UID 10450; state: ENABLED
2026-03-29 16:00:50.340  3374-3374  GearBoardMidi           com.gearboard                        D  Found 1 MIDI devices
2026-03-29 16:00:50.342  3374-3374  GearBoardMidi           com.gearboard                        D  Found 1 MIDI devices
2026-03-29 16:00:50.433  3374-3388  com.gearboard           com.gearboard                        I  Compiler allocated 6236KB to compile void android.view.ViewRootImpl.performTraversals()
2026-03-29 16:00:50.526  3374-3374  com.gearboard           com.gearboard                        W  Method boolean androidx.compose.runtime.snapshots.SnapshotStateList.conditionalUpdate(boolean, kotlin.jvm.functions.Function1) failed lock verification and will run slower.
Common causes for lock verification issues are non-optimized dex code
and incorrect proguard optimizations.
2026-03-29 16:00:50.526  3374-3374  com.gearboard           com.gearboard                        W  Method boolean androidx.compose.runtime.snapshots.SnapshotStateList.conditionalUpdate$default(androidx.compose.runtime.snapshots.SnapshotStateList, boolean, kotlin.jvm.functions.Function1, int, java.lang.Object) failed lock verification and will run slower.
2026-03-29 16:00:50.526  3374-3374  com.gearboard           com.gearboard                        W  Method java.lang.Object androidx.compose.runtime.snapshots.SnapshotStateList.mutate(kotlin.jvm.functions.Function1) failed lock verification and will run slower.
2026-03-29 16:00:50.526  3374-3374  com.gearboard           com.gearboard                        W  Method void androidx.compose.runtime.snapshots.SnapshotStateList.update(boolean, kotlin.jvm.functions.Function1) failed lock verification and will run slower.
2026-03-29 16:00:50.526  3374-3374  com.gearboard           com.gearboard                        W  Method void androidx.compose.runtime.snapshots.SnapshotStateList.update$default(androidx.compose.runtime.snapshots.SnapshotStateList, boolean, kotlin.jvm.functions.Function1, int, java.lang.Object) failed lock verification and will run slower.
2026-03-29 16:00:50.564  3374-3374  GearBoardMidi           com.gearboard                        D  Found 1 MIDI devices
2026-03-29 16:00:50.627  3374-3374  Compatibil...geReporter com.gearboard                        D  Compat change id reported: 171228096; UID 10450; state: ENABLED
2026-03-29 16:00:50.726   949-1637  BufferQueueDebug        surfaceflinger                       E  [com.gearboard/com.gearboard.MainActivity#24856](this:0xb4000073d693da80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'com.gearboard/com.gearboard.MainActivity#24856'
2026-03-29 16:00:50.734  3374-3374  BufferQueueConsumer     com.gearboard                        D  [](id:d2e00000000,api:0,p:-1,c:3374) connect: controlledByApp=false
2026-03-29 16:00:50.738  3374-3374  ForceDarkHelperStubImpl com.gearboard                        I  setViewRootImplForceDark: false for com.gearboard.MainActivity@cd6a74e, reason: AppDarkModeEnable
2026-03-29 16:00:50.738  3374-3427  libMEOW                 com.gearboard                        D  meow new tls: 0xb40000739b257680
2026-03-29 16:00:50.738  3374-3427  libMEOW                 com.gearboard                        D  applied 1 plugins for [com.gearboard]:
2026-03-29 16:00:50.738  3374-3427  libMEOW                 com.gearboard                        D    plugin 1: [libMEOW_gift.so]: 0xb40000739af69480
2026-03-29 16:00:50.738  3374-3427  libMEOW                 com.gearboard                        D  rebuild call chain: 0xb40000739b2664c0
2026-03-29 16:00:50.760  3374-3427  OpenGLRenderer          com.gearboard                        E  Unable to match the desired swap behavior.
2026-03-29 16:00:50.773  3374-3374  VRI[MainActivity]       com.gearboard                        D  vri.reportNextDraw android.view.ViewRootImpl.performTraversals:4460 android.view.ViewRootImpl.doTraversal:3069 android.view.ViewRootImpl$TraversalRunnable.run:10654 android.view.Choreographer$CallbackRecord.run:1768 android.view.Choreographer$CallbackRecord.run:1777
2026-03-29 16:00:50.773  3374-3374  VRI[MainActivity]       com.gearboard                        D  vri.Setup new sync=wmsSync-VRI[MainActivity]#0
2026-03-29 16:00:50.809  3374-3427  OpenGLRenderer          com.gearboard                        D  makeCurrent grContext:0xb40000739b2e2e80 reset mTextureAvailable
2026-03-29 16:00:50.810  3374-3427  com.gearboard           com.gearboard                        D  MiuiProcessManagerServiceStub setSchedFifo
2026-03-29 16:00:50.810  3374-3427  MiuiProcessManagerImpl  com.gearboard                        I  setSchedFifo pid:3374, mode:3
2026-03-29 16:00:50.810  3374-3427  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.magt.mtk_magt_support"
2026-03-29 16:00:50.811  3374-3427  MAGT_SYNC_FRAME         com.gearboard                        D  MAGT Sync: MAGT is not supported. Disabling Sync.
2026-03-29 16:00:50.812  3374-3427  hw-ProcessState         com.gearboard                        D  Binder ioctl to enable oneway spam detection failed: Invalid argument
2026-03-29 16:00:50.814  3374-3427  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.display.iris_x7.support"
2026-03-29 16:00:50.856  3374-3427  LB                      com.gearboard                        E  fail to open file: No such file or directory
2026-03-29 16:00:50.856  3374-3427  LB                      com.gearboard                        E  fail to open node: No such file or directory
2026-03-29 16:00:50.856  3374-3427  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.display.iris_x7.support"
2026-03-29 16:00:50.857  3374-3427  BLASTBufferQueue        com.gearboard                        D  [VRI[MainActivity]#0](f:0,a:1) acquireNextBufferLocked size=2306x1080 mFrameNumber=1 applyTransaction=true mTimestamp=174567411653481(auto) mPendingTransactions.size=0 graphicBufferId=14491219656708 transform=7
2026-03-29 16:00:50.858  3374-3374  VRI[MainActivity]       com.gearboard                        D  vri.reportDrawFinished
2026-03-29 16:00:50.859  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:50.859  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:50.859  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:50.861   949-1635  BufferQueueDebug        surfaceflinger                       E  [Surface(name=a8ea596 com.gearboard/com.gearboard.MainActivity#24853)/@0xce63d04 - animation-leash of starting_reveal#24859](this:0xb4000073d6c87a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'Surface(name=a8ea596 com.gearboard/com.gearboard.MainActivity#24853)/@0xce63d04 - animation-leash of starting_reveal#24859'
2026-03-29 16:00:50.876  3374-3408  OpenGLRenderer          com.gearboard                        I  Davey! duration=938ms; Flags=1, FrameTimelineVsyncId=85667503, IntendedVsync=174566475037176, Vsync=174566689919116, InputEventId=0, HandleInputStart=174566700313096, AnimationStart=174566700322019, PerformTraversalsStart=174566700604558, DrawStart=174567329912788, FrameDeadline=174566495037176, FrameInterval=174566699610942, FrameStartTime=16529380, FlingPosition=0, SyncQueued=174567364127250, SyncStart=174567364287250, IssueDrawCommandsStart=174567365688865, SwapBuffers=174567408823558, FrameCompleted=174567413493788, DequeueBufferDuration=880307, QueueBufferDuration=379077, GpuCompleted=174567413493788, SwapBuffersCompleted=174567412201481, DisplayPresentTime=35192962673642, CommandSubmissionCompleted=174567408823558,
2026-03-29 16:00:50.881  3374-3374  VRI[MainActivity]       com.gearboard                        D  vri.reportNextDraw android.view.ViewRootImpl.handleResized:2368 android.view.ViewRootImpl.-$$Nest$mhandleResized:0 android.view.ViewRootImpl$ViewRootHandler.handleMessageImpl:6889 android.view.ViewRootImpl$ViewRootHandler.handleMessage:6858 android.os.Handler.dispatchMessage:106
2026-03-29 16:00:50.882  3374-3374  Choreographer           com.gearboard                        I  Skipped 42 frames!  The application may be doing too much work on its main thread.
2026-03-29 16:00:50.882   949-980   BufferQueueDebug        surfaceflinger                       E  [Surface(name=889c6ae Splash Screen com.gearboard#24845)/@0x14b68e5 - animation-leash of window_animation#24860](this:0xb4000073dbd54a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'Surface(name=889c6ae Splash Screen com.gearboard#24845)/@0x14b68e5 - animation-leash of window_animation#24860'
2026-03-29 16:00:50.956  3374-3374  VRI[MainActivity]       com.gearboard                        D  vri.Setup new sync=wmsSync-VRI[MainActivity]#2
2026-03-29 16:00:50.967  3374-3427  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.display.iris_x7.support"
2026-03-29 16:00:50.973  3374-3374  VRI[MainActivity]       com.gearboard                        D  vri.reportDrawFinished
2026-03-29 16:00:50.973  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:50.973  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:50.978  3374-3374  HandWritingStubImpl     com.gearboard                        I  refreshLastKeyboardType: 1
2026-03-29 16:00:50.978  3374-3374  HandWritingStubImpl     com.gearboard                        I  getCurrentKeyboardType: 1
2026-03-29 16:00:50.986  3374-3402  OpenGLRenderer          com.gearboard                        I  Davey! duration=790ms; Flags=0, FrameTimelineVsyncId=85667813, IntendedVsync=174566739519679, Vsync=174567433776571, InputEventId=0, HandleInputStart=174567437327327, AnimationStart=174567437332481, PerformTraversalsStart=174567510755558, DrawStart=174567511227635, FrameDeadline=174567437211836, FrameInterval=174567436929404, FrameStartTime=16529926, FlingPosition=0, SyncQueued=174567521904558, SyncStart=174567521963019, IssueDrawCommandsStart=174567522242096, SwapBuffers=174567526914019, FrameCompleted=174567529934096, DequeueBufferDuration=1465923, QueueBufferDuration=203077, GpuCompleted=174567529934096, SwapBuffersCompleted=174567527723327, DisplayPresentTime=35188667398948, CommandSubmissionCompleted=174567526914019,
2026-03-29 16:00:51.049  3374-3374  HandWritingStubImpl     com.gearboard                        I  getCurrentKeyboardType: 1
2026-03-29 16:00:51.468  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:51.468  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:51.468  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:51.474  3374-3427  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.display.iris_x7.support"
2026-03-29 16:00:51.520  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:51.524  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:51.576  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:51.580  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:51.817  3374-3374  MiuiMultiWindowUtils    com.gearboard                        D  freeform resolution args raw data:{  "wide_default":{    "freeform_args": {        "vertical_portrait":{"aspect_ratio":0.626, "original_ratio":0.5643,"original_scale":0.74,"top_margin":0.168,"left_margin":0.484},        "horizontal_portrait":{"aspect_ratio":0.626, "original_ratio":0.5643,"original_scale":0.74,"top_margin":0.1222,"left_margin":0.59745},        "vertical_landscape":{"aspect_ratio":1.6, "original_ratio":1,"original_scale":0.604,"top_margin":0.2596,"left_margin":0.2624},        "horizontal_landscape":{"aspect_ratio":1.6, "original_ratio":1,"original_scale":0.604,"top_margin":0.213,"left_margin":0.3758}    },    "mini_freeform_args":{        "vertical_portrait":{"original_ratio":0.147},        "horizontal_portrait":{"original_ratio":0.147},        "vertical_landscape":{"original_ratio":0.165},        "horizontal_landscape":{"original_ratio":0.165}    }  },  "narrow_default": {    "freeform_args": {        "vertical_portrait":{"aspect_ratio":0.626, "original_ratio":1,"original_scale":0.74,"top_margin":0.0753,"left_margin":-1},        "horizontal_portrait":{"aspect_ratio":0.626, "original_ratio":1,"original_scale":0.5756,"top_margin":-1,"left_margin":0.0753},        "vertical_landscape":{"aspect_ratio":1.6, "original_ratio":0.6847,"original_scale":0.587,"top_margin":0.0753,"left_margin":-1},        "horizontal_landscape":{"aspect_ratio":1.6, "original_ratio":0.6847,"original_scale":0.587,"top_margin":-1,"left_margin":0.0753}    },    "mini_freeform_args":{        "vertical_portrait":{"original_ratio":0.26},        "horizontal_portrait":{"original_ratio":0.26},        "vertical_landscape":{"original_ratio":0.293},        "horizontal_landscape":{"original_ratio":0.293}    }  },  "regular_default": {    "freeform_args": {      "vertical_portrait":{"aspect_ratio":0.625, "original_ratio":1,"original_scale":0.7,"top_margin":0.109,"left_margin":-1},      "horizontal_portrait":{"aspect_ratio":0.6667, "original_ratio":1,"original_scale":0.6102,"top_margin":-1,"left_margin":0.026},      "vertical_landscape":{"aspect_ratio":1.6, "original_ratio":1,"original_scale":0.4244,"top_margin":0.109,"left_margin":-1},      "horizontal_landscape":{"aspect_ratio":1.6, "original_ratio":1,"original_scale":0.4244,"top_margin":-1,"left_margin":0.026}    },    "mini_freeform_args":{      "vertical_portrait":{"original_ratio":0.25},      "horizontal_portrait":{"original_ratio":0.25},      "vertical_landscape":{"original_ratio":0.25},      "horizontal_landscape":{"original_ratio":0.25}    }  },  "pad_default": {    "freeform_args": {      "vertical_portrait":{"aspect_ratio":0.5625, "original_ratio":0.375,"original_scale":0.835,"top_margin":0.049,"left_margin":0.2775},      "horizontal_portrait":{"aspect_ratio":0.5625, "original_ratio":0.375,"original_scale":0.835,"top_margin":-1,"left_margin":0.6525},      "vertical_landscape":{"aspect_ratio":-1, "original_ratio":1,"original_scale":0.468,"top_margin":0.049,"left_margin":-1},      "horizontal_landscape":{"aspect_ratio":-1, "original_ratio":1,"original_scale":0.468,"top_margin":-1,"left_margin":0.4976}    },    "mini_freeform_args":{      "vertical_portrait":{"original_ratio":0.144},      "horizontal_portrait":{"original_ratio":0.144},      "vertical_landscape":{"original_ratio":0.2},      "horizontal_landscape":{"original_ratio":0.2}    }  }}
2026-03-29 16:00:51.820  3374-3374  MiuiMultiWindowUtils    com.gearboard                        D  initFreeFormResolutionArgs failed, device is rubypro
2026-03-29 16:00:51.820  3374-3374  IS_CTS_MODE             com.gearboard                        D  false
2026-03-29 16:00:51.820  3374-3374  MULTI_WIND...CH_ENABLED com.gearboard                        D  false
2026-03-29 16:00:51.892  3374-3374  GearBoardMidi           com.gearboard                        D  Found 1 MIDI devices
2026-03-29 16:00:51.911  3374-3374  WindowOnBackDispatcher  com.gearboard                        W  OnBackInvokedCallback is not enabled for the application.
Set 'android:enableOnBackInvokedCallback="true"' in the application manifest.
2026-03-29 16:00:51.998  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:52.001  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.010  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.027  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.044  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.060  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.075  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.092  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.109  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.125  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.142  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.158  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.192  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.196  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:52.209  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.225  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.242  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.258  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.274  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.298  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:52.309  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.314  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.320  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.337  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.353  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.370  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.386  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.402  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.419  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.435  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.452  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.469  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.485  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.502  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.518  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.535  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.551  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.568  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.587  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.605  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.621  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.638  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.654  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.671  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.687  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.705  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.721  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.755  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.773  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.788  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.804  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.820  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.836  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.851  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:52.852  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.870  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.886  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.901  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.915  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:52.956  3374-3374  ForceDarkHelperStubImpl com.gearboard                        I  setViewRootImplForceDark: false for com.gearboard.MainActivity@cd6a74e, reason: AppDarkModeEnable
2026-03-29 16:00:52.959  3374-3374  VRI[MainActivity]       com.gearboard                        D  vri.reportNextDraw android.view.ViewRootImpl.handleResized:2368 android.view.ViewRootImpl.-$$Nest$mhandleResized:0 android.view.ViewRootImpl$ViewRootHandler.handleMessageImpl:6889 android.view.ViewRootImpl$ViewRootHandler.handleMessage:6858 android.os.Handler.dispatchMessage:106
2026-03-29 16:00:53.086  3374-3427  OpenGLRenderer          com.gearboard                        E  Unable to match the desired swap behavior.
2026-03-29 16:00:53.097  3374-3374  VRI[MainActivity]       com.gearboard                        D  vri.reportNextDraw android.view.ViewRootImpl.performTraversals:4460 android.view.ViewRootImpl.doTraversal:3069 android.view.ViewRootImpl$TraversalRunnable.run:10654 android.view.Choreographer$CallbackRecord.run:1768 android.view.Choreographer$CallbackRecord.run:1777
2026-03-29 16:00:53.097  3374-3374  VRI[MainActivity]       com.gearboard                        D  vri.Setup new sync=wmsSync-VRI[MainActivity]#4
2026-03-29 16:00:53.113  3374-3427  BLASTBufferQueue        com.gearboard                        D  [VRI[MainActivity]#0](f:0,a:2) producer disconnected before acquireNextBufferLocked
2026-03-29 16:00:53.114  3374-3374  VRI[MainActivity]       com.gearboard                        D  vri.reportDrawFinished
2026-03-29 16:00:53.114  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:55.190  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:55.190  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:55.193  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.217  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.268  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.285  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.300  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.316  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.329  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:55.334  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.349  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.366  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.389  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.399  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.417  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.428  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.451  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.451  3374-3501  ProfileInstaller        com.gearboard                        D  Installing profile for com.gearboard
2026-03-29 16:00:55.469  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.479  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.497  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.514  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.531  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.548  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.565  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.582  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.598  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.614  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.631  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.648  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.664  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.681  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.697  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.714  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.730  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.747  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.763  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.780  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.796  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.812  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.829  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.846  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.866  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:55.958  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:55.962  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:00:56.022  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:00:56.025  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:02.849  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:02.877  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:02.902  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:02.909  3374-3374  ScrollIdentify          com.gearboard                        I  on fling
2026-03-29 16:01:02.923  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:02.926  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:02.936  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:02.952  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:02.969  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:02.986  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.003  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.020  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.036  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.053  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.069  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.086  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.102  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.119  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.135  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.152  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.168  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.185  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.218  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.247  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.434  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.451  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.484  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.508  3374-3374  ScrollIdentify          com.gearboard                        I  on fling
2026-03-29 16:01:03.524  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.533  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.549  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.565  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.582  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.598  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.615  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.631  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.648  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.665  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.680  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.697  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.714  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.730  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.747  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.763  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.780  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.796  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.813  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.829  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.846  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.863  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.878  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.895  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.912  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.929  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.945  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:03.978  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.007  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.590  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.607  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.624  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.640  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.656  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.673  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.689  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.698  3374-3427  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.display.iris_x7.support"
2026-03-29 16:01:04.699  3374-3374  ForceDarkHelperStubImpl com.gearboard                        I  setViewRootImplForceDark: false for com.gearboard.MainActivity@cd6a74e, reason: AppDarkModeEnable
2026-03-29 16:01:04.699  3374-3374  VRI[Спливаюче вікно]    com.gearboard                        D  hardware acceleration = true, forceHwAccelerated = false
2026-03-29 16:01:04.705  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.722  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.731  3374-3374  WindowOnBackDispatcher  com.gearboard                        W  OnBackInvokedCallback is not enabled for the application.
Set 'android:enableOnBackInvokedCallback="true"' in the application manifest.
2026-03-29 16:01:04.739  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.755  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.772  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.788  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.804  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.821  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.826  3374-3374  BufferQueueConsumer     com.gearboard                        D  [](id:d2e00000001,api:0,p:-1,c:3374) connect: controlledByApp=false
2026-03-29 16:01:04.827  3374-3374  ForceDarkHelperStubImpl com.gearboard                        I  setViewRootImplForceDark: false for com.gearboard.MainActivity@cd6a74e, reason: AppDarkModeEnable
2026-03-29 16:01:04.827  3374-3427  OpenGLRenderer          com.gearboard                        E  Unable to match the desired swap behavior.
2026-03-29 16:01:04.837  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.838  3374-3374  VRI[Спливаюче вікно]    com.gearboard                        D  vri.reportNextDraw android.view.ViewRootImpl.performTraversals:4460 android.view.ViewRootImpl.doTraversal:3069 android.view.ViewRootImpl$TraversalRunnable.run:10654 android.view.Choreographer$CallbackRecord.run:1768 android.view.Choreographer$CallbackRecord.run:1777
2026-03-29 16:01:04.838  3374-3374  VRI[Спливаюче вікно]    com.gearboard                        D  vri.Setup new sync=wmsSync-VRI[Спливаюче вікно]#6
2026-03-29 16:01:04.854  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.854  3374-3427  OpenGLRenderer          com.gearboard                        D  makeCurrent grContext:0xb40000739b2e2e80 reset mTextureAvailable
2026-03-29 16:01:04.856  3374-3427  BLASTBufferQueue        com.gearboard                        D  [VRI[Спливаюче вікно]#1](f:0,a:1) acquireNextBufferLocked size=1080x2400 mFrameNumber=1 applyTransaction=true mTimestamp=174581411576558(auto) mPendingTransactions.size=0 graphicBufferId=14491219656719 transform=0
2026-03-29 16:01:04.857  3374-3374  VRI[Спливаюче вікно]    com.gearboard                        D  vri.reportDrawFinished
2026-03-29 16:01:04.857  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:04.872  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.874  3374-3374  VRI[Спливаюче вікно]    com.gearboard                        D  vri.reportNextDraw android.view.ViewRootImpl.handleResized:2368 android.view.ViewRootImpl.-$$Nest$mhandleResized:0 android.view.ViewRootImpl$ViewRootHandler.handleMessageImpl:6889 android.view.ViewRootImpl$ViewRootHandler.handleMessage:6858 android.os.Handler.dispatchMessage:106
2026-03-29 16:01:04.879  3374-3374  HandWritingStubImpl     com.gearboard                        I  refreshLastKeyboardType: 1
2026-03-29 16:01:04.879  3374-3374  HandWritingStubImpl     com.gearboard                        I  getCurrentKeyboardType: 1
2026-03-29 16:01:04.887  3374-3374  VRI[Спливаюче вікно]    com.gearboard                        D  vri.Setup new sync=wmsSync-VRI[Спливаюче вікно]#8
2026-03-29 16:01:04.889  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.892  3374-3374  VRI[Спливаюче вікно]    com.gearboard                        D  vri.reportDrawFinished
2026-03-29 16:01:04.901  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.918  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.935  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.938  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.950  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.968  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.972  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.985  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:04.989  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.001  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.004  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.019  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.023  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.034  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.038  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.052  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.056  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.067  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.071  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.084  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.088  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.100  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.104  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.117  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.121  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.133  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.137  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.150  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.154  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.167  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.170  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.183  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.187  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.200  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.203  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.216  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.220  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.232  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.236  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.249  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.252  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.265  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.269  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.284  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.287  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.300  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.317  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.334  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.351  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.367  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.383  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.400  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.416  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.433  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.450  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.466  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.483  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.500  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.516  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.533  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.546  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:05.562  3374-3427  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:07.310  3374-3374  WindowOnBackDispatcher  com.gearboard                        W  sendCancelIfRunning: isInProgress=falsecallback=android.view.ViewRootImpl$$ExternalSyntheticLambda23@d1a8c6
2026-03-29 16:01:07.313  3374-3374  View                    com.gearboard                        D  [Warning] assignParent to null: this = androidx.compose.material3.ModalBottomSheetWindow{fced1cc V.E...... ......ID 0,0-1080,2400 #1020002 android:id/content aid=1073741824}
2026-03-29 16:01:07.313  3374-3374  BLASTBufferQueue        com.gearboard                        D  [VRI[Спливаюче вікно]#1](f:0,a:1) destructor()
2026-03-29 16:01:07.313  3374-3374  BufferQueueConsumer     com.gearboard                        D  [VRI[Спливаюче вікно]#1(BLAST Consumer)1](id:d2e00000001,api:0,p:-1,c:3374) disconnect
2026-03-29 16:01:07.359  3374-3374  AndroidRuntime          com.gearboard                        D  Shutting down VM
2026-03-29 16:01:07.361  3374-3374  AndroidRuntime          com.gearboard                        E  FATAL EXCEPTION: main
Process: com.gearboard, PID: 3374
java.lang.IllegalArgumentException: LazyVerticalGrid's width should be bound by parent.
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke-0kLqBqw(LazyGridDsl.kt:155)
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke(LazyGridDsl.kt:154)
at androidx.compose.foundation.lazy.grid.GridSlotCache.invoke-0kLqBqw(LazyGridDsl.kt:235)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke-0kLqBqw(LazyGrid.kt:216)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke(LazyGrid.kt:177)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.SizeNode.measure-3p2s80s(Size.kt:838)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:251)
at androidx.compose.runtime.snapshots.Snapshot$Companion.observe(Snapshot.kt:2303)
at androidx.compose.runtime.snapshots.SnapshotStateObserver$ObservedScopeMap.observe(SnapshotStateObserver.kt:500)
at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:256)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeReads$ui_release(OwnerSnapshotObserver.kt:133)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeMeasureSnapshotReads$ui_release(OwnerSnapshotObserver.kt:113)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:1617)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.access$performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:36)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:620)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.measure-BRTryo0(LayoutNodeLayoutDelegate.kt:596)
at androidx.compose.foundation.layout.RowColumnMeasurementHelper.measureWithoutPlacing-_EkL_-Y(RowColumnMeasurementHelper.kt:112)
at androidx.compose.foundation.layout.RowColumnMeasurePolicy.measure-3p2s80s(RowColumnImpl.kt:72)
2026-03-29 16:01:07.361  3374-3374  AndroidRuntime          com.gearboard                        E  	at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.layout.PaddingNode.measure-3p2s80s(Padding.kt:397)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.SizeNode.measure-3p2s80s(Size.kt:838)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.PaddingNode.measure-3p2s80s(Padding.kt:397)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:251)
at androidx.compose.runtime.snapshots.Snapshot$Companion.observe(Snapshot.kt:2303)
at androidx.compose.runtime.snapshots.SnapshotStateObserver$ObservedScopeMap.observe(SnapshotStateObserver.kt:500)
at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:256)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeReads$ui_release(OwnerSnapshotObserver.kt:133)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeMeasureSnapshotReads$ui_release(OwnerSnapshotObserver.kt:113)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:1617)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.access$performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:36)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:620)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.measure-BRTryo0(LayoutNodeLayoutDelegate.kt:596)
at androidx.compose.foundation.lazy.layout.LazyLayoutMeasureScopeImpl.measure-0kLqBqw(LazyLayoutMeasureScope.kt:127)
at androidx.compose.foundation.lazy.LazyListMeasuredItemProvider.getAndMeasure(LazyListMeasuredItemProvider.kt:48)
at androidx.compose.foundation.lazy.LazyListMeasureKt.measureLazyList-5IMabDg(LazyListMeasure.kt:195)
at androidx.compose.foundation.lazy.LazyListKt$rememberLazyListMeasurePolicy$1$1.invoke-0kLqBqw(LazyList.kt:313)
at androidx.compose.foundation.lazy.LazyListKt$rememberLazyListMeasurePolicy$1$1.invoke(LazyList.kt:178)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
2026-03-29 16:01:07.361  3374-3374  AndroidRuntime          com.gearboard                        E  	at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:251)
at androidx.compose.runtime.snapshots.Snapshot$Companion.observe(Snapshot.kt:2303)
at androidx.compose.runtime.snapshots.SnapshotStateObserver$ObservedScopeMap.observe(SnapshotStateObserver.kt:500)
at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:256)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeReads$ui_release(OwnerSnapshotObserver.kt:133)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeMeasureSnapshotReads$ui_release(OwnerSnapshotObserver.kt:113)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:1617)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.access$performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:36)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:620)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.measure-BRTryo0(LayoutNodeLayoutDelegate.kt:596)
at androidx.compose.foundation.layout.RowColumnMeasurementHelper.measureWithoutPlacing-_EkL_-Y(RowColumnMeasurementHelper.kt:112)
at androidx.compose.foundation.layout.RowColumnMeasurePolicy.measure-3p2s80s(RowColumnImpl.kt:72)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.layout.FillNode.measure-3p2s80s(Size.kt:699)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:251)
at androidx.compose.runtime.snapshots.Snapshot$Companion.observe(Snapshot.kt:2303)
at androidx.compose.runtime.snapshots.SnapshotStateObserver$ObservedScopeMap.observe(SnapshotStateObserver.kt:500)
at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:256)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeReads$ui_release(OwnerSnapshotObserver.kt:133)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeMeasureSnapshotReads$ui_release(OwnerSnapshotObserver.kt:113)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:1617)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.access$performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:36)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:620)
at androidx.compose.ui.node.LayoutNode.remeasure-_Sx5XlM$ui_release(LayoutNode.kt:1145)
at androidx.compose.ui.node.LayoutNode.remeasure-_Sx5XlM$ui_release$default(LayoutNode.kt:1136)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.doRemeasure-sdFAvZA(MeasureAndLayoutDelegate.kt:356)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.remeasureAndRelayoutIfNeeded(MeasureAndLayoutDelegate.kt:514)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.onlyRemeasureIfScheduled(MeasureAndLayoutDelegate.kt:598)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.forceMeasureTheSubtreeInternal(MeasureAndLayoutDelegate.kt:624)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.forceMeasureTheSubtreeInternal(MeasureAndLayoutDelegate.kt:631)
2026-03-29 16:01:07.362  3374-3374  AndroidRuntime          com.gearboard                        E  	at androidx.compose.ui.node.MeasureAndLayoutDelegate.forceMeasureTheSubtreeInternal(MeasureAndLayoutDelegate.kt:631)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.forceMeasureTheSubtreeInternal(MeasureAndLayoutDelegate.kt:631)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.forceMeasureTheSubtree(MeasureAndLayoutDelegate.kt:587)
at androidx.compose.ui.platform.AndroidComposeView.forceMeasureTheSubtree(AndroidComposeView.android.kt:993)
at androidx.compose.ui.node.Owner.forceMeasureTheSubtree$default(Owner.kt:239)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:632)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.measure-BRTryo0(LayoutNodeLayoutDelegate.kt:596)
at androidx.compose.foundation.lazy.layout.LazyLayoutMeasureScopeImpl.measure-0kLqBqw(LazyLayoutMeasureScope.kt:127)
at androidx.compose.foundation.lazy.LazyListMeasuredItemProvider.getAndMeasure(LazyListMeasuredItemProvider.kt:48)
at androidx.compose.foundation.lazy.LazyListMeasureKt.measureLazyList-5IMabDg(LazyListMeasure.kt:195)
at androidx.compose.foundation.lazy.LazyListKt$rememberLazyListMeasurePolicy$1$1.invoke-0kLqBqw(LazyList.kt:313)
at androidx.compose.foundation.lazy.LazyListKt$rememberLazyListMeasurePolicy$1$1.invoke(LazyList.kt:178)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.PaddingValuesModifier.measure-3p2s80s(Padding.kt:455)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.FillNode.measure-3p2s80s(Size.kt:699)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:251)
at androidx.compose.runtime.snapshots.Snapshot$Companion.observe(Snapshot.kt:2303)
at androidx.compose.runtime.snapshots.SnapshotStateObserver$ObservedScopeMap.observe(SnapshotStateObserver.kt:500)
at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:256)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeReads$ui_release(OwnerSnapshotObserver.kt:133)
2026-03-29 16:01:07.362  3374-3374  AndroidRuntime          com.gearboard                        E  	at androidx.compose.ui.node.OwnerSnapshotObserver.observeMeasureSnapshotReads$ui_release(OwnerSnapshotObserver.kt:113)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:1617)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.access$performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:36)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:620)
at androidx.compose.ui.node.LayoutNode.remeasure-_Sx5XlM$ui_release(LayoutNode.kt:1145)
at androidx.compose.ui.node.LayoutNode.remeasure-_Sx5XlM$ui_release$default(LayoutNode.kt:1136)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.doRemeasure-sdFAvZA(MeasureAndLayoutDelegate.kt:356)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.remeasureAndRelayoutIfNeeded(MeasureAndLayoutDelegate.kt:514)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.remeasureAndRelayoutIfNeeded$default(MeasureAndLayoutDelegate.kt:491)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.measureAndLayout(MeasureAndLayoutDelegate.kt:377)
at androidx.compose.ui.platform.AndroidComposeView.measureAndLayout(AndroidComposeView.android.kt:971)
at androidx.compose.ui.node.Owner.measureAndLayout$default(Owner.kt:228)
at androidx.compose.ui.platform.AndroidComposeView.dispatchDraw(AndroidComposeView.android.kt:1224)
at android.view.View.draw(View.java:25001)
at android.view.View.updateDisplayListIfDirty(View.java:23784)
at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:4668)
at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:4641)
at android.view.View.updateDisplayListIfDirty(View.java:23709)
at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:4668)
at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:4641)
at android.view.View.updateDisplayListIfDirty(View.java:23709)
at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:4668)
at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:4641)
at android.view.View.updateDisplayListIfDirty(View.java:23709)
at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:4668)
at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:4641)
at android.view.View.updateDisplayListIfDirty(View.java:23709)
at android.view.ThreadedRenderer.updateViewTreeDisplayList(ThreadedRenderer.java:736)
at android.view.ThreadedRenderer.updateRootDisplayList(ThreadedRenderer.java:745)
at android.view.ThreadedRenderer.draw(ThreadedRenderer.java:855)
at android.view.ViewRootImpl.draw(ViewRootImpl.java:5796)
at android.view.ViewRootImpl.performDraw(ViewRootImpl.java:5439)
at android.view.ViewRootImpl.performTraversals(ViewRootImpl.java:4519)
at android.view.ViewRootImpl.doTraversal(ViewRootImpl.java:3069)
at android.view.ViewRootImpl$TraversalRunnable.run(ViewRootImpl.java:10654)
at android.view.Choreographer$CallbackRecord.run(Choreographer.java:1768)
at android.view.Choreographer$CallbackRecord.run(Choreographer.java:1777)
at android.view.Choreographer.doCallbacks(Choreographer.java:1247)
at android.view.Choreographer.doFrame(Choreographer.java:1114)
at android.view.Choreographer$FrameDisplayEventReceiver.run(Choreographer.java:1731)
at android.os.Handler.handleCallback(Handler.java:958)
at android.os.Handler.dispatchMessage(Handler.java:99)
at android.os.Looper.loopOnce(Looper.java:222)
at android.os.Looper.loop(Looper.java:314)
at android.app.ActivityThread.main(ActivityThread.java:8788)
at java.lang.reflect.Method.invoke(Native Method)
at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:569)
at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1090)
2026-03-29 16:01:07.364  3374-3374  ScoutUtils              com.gearboard                        W  Failed to mkdir /data/miuilog/stability/memleak/heapdump/
2026-03-29 16:01:07.361  3374-3374  com.gearboard           com.gearboard                        W  type=1400 audit(0.0:465483): avc:  denied  { search } for  name="miuilog" dev="dm-52" ino=356 scontext=u:r:untrusted_app:s0:c194,c257,c512,c768 tcontext=u:object_r:data_log_file:s0 tclass=dir permissive=0 app=com.gearboard
2026-03-29 16:01:07.361  3374-3374  com.gearboard           com.gearboard                        W  type=1400 audit(0.0:465484): avc:  denied  { search } for  name="miuilog" dev="dm-52" ino=356 scontext=u:r:untrusted_app:s0:c194,c257,c512,c768 tcontext=u:object_r:data_log_file:s0 tclass=dir permissive=0 app=com.gearboard
2026-03-29 16:01:07.361  3374-3374  com.gearboard           com.gearboard                        W  type=1400 audit(0.0:465485): avc:  denied  { search } for  name="miuilog" dev="dm-52" ino=356 scontext=u:r:untrusted_app:s0:c194,c257,c512,c768 tcontext=u:object_r:data_log_file:s0 tclass=dir permissive=0 app=com.gearboard
2026-03-29 16:01:07.361  3374-3374  com.gearboard           com.gearboard                        W  type=1400 audit(0.0:465486): avc:  denied  { search } for  name="miuilog" dev="dm-52" ino=356 scontext=u:r:untrusted_app:s0:c194,c257,c512,c768 tcontext=u:object_r:data_log_file:s0 tclass=dir permissive=0 app=com.gearboard
2026-03-29 16:01:07.361  3374-3374  com.gearboard           com.gearboard                        W  type=1400 audit(0.0:465487): avc:  denied  { search } for  name="miuilog" dev="dm-52" ino=356 scontext=u:r:untrusted_app:s0:c194,c257,c512,c768 tcontext=u:object_r:data_log_file:s0 tclass=dir permissive=0 app=com.gearboard
2026-03-29 16:01:07.361  3374-3374  com.gearboard           com.gearboard                        W  type=1400 audit(0.0:465488): avc:  denied  { search } for  name="miuilog" dev="dm-52" ino=356 scontext=u:r:untrusted_app:s0:c194,c257,c512,c768 tcontext=u:object_r:data_log_file:s0 tclass=dir permissive=0 app=com.gearboard
2026-03-29 16:01:07.369  4743-5061  JavaExceptionHandler    com.miui.daemon                      E  Process: com.gearboard, PID: 3374
java.lang.IllegalArgumentException: LazyVerticalGrid's width should be bound by parent.
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke-0kLqBqw(LazyGridDsl.kt:155)
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke(LazyGridDsl.kt:154)
at androidx.compose.foundation.lazy.grid.GridSlotCache.invoke-0kLqBqw(LazyGridDsl.kt:235)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke-0kLqBqw(LazyGrid.kt:216)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke(LazyGrid.kt:177)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.SizeNode.measure-3p2s80s(Size.kt:838)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:251)
at androidx.compose.runtime.snapshots.Snapshot$Companion.observe(Snapshot.kt:2303)
at androidx.compose.runtime.snapshots.SnapshotStateObserver$ObservedScopeMap.observe(SnapshotStateObserver.kt:500)
at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:256)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeReads$ui_release(OwnerSnapshotObserver.kt:133)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeMeasureSnapshotReads$ui_release(OwnerSnapshotObserver.kt:113)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:1617)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.access$performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:36)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:620)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.measure-BRTryo0(LayoutNodeLayoutDelegate.kt:596)
at androidx.compose.foundation.layout.RowColumnMeasurementHelper.measureWithoutPlacing-_EkL_-Y(RowColumnMeasurementHelper.kt:112)
at androidx.compose.foundation.layout.RowColumnMeasurePolicy.measure-3p2s80s(RowColumnImpl.kt:72)
2026-03-29 16:01:07.391  3374-3374  BinderMonitor           com.gearboard                        E  err open binder_delay errno=2
2026-03-29 16:01:07.391  3374-3374  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:07.391  3374-3374  Process                 com.gearboard                        I  Process is going to kill itself!
java.lang.Exception
at android.os.Process.killProcess(Process.java:1346)
at com.android.internal.os.RuntimeInit$KillApplicationHandler.uncaughtException(RuntimeInit.java:187)
at java.lang.ThreadGroup.uncaughtException(ThreadGroup.java:1098)
at java.lang.ThreadGroup.uncaughtException(ThreadGroup.java:1093)
at java.lang.Thread.dispatchUncaughtException(Thread.java:3377)
2026-03-29 16:01:07.392  3374-3374  Process                 com.gearboard                        I  Sending signal. PID: 3374 SIG: 9
2026-03-29 16:01:08.432  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8209   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=268435456 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 16:01:09.346   949-980   BufferQueueDebug        surfaceflinger                       E  [ActivityRecord{8bf4ee3 u0 com.gearboard/.MainActivity t8210}#24892](this:0xb4000073dbd68a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'ActivityRecord{8bf4ee3 u0 com.gearboard/.MainActivity t8210}#24892'
2026-03-29 16:01:09.358   949-979   BufferQueueDebug        surfaceflinger                       E  [c15fc55 Splash Screen com.gearboard#24893](this:0xb4000073d6c62a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'c15fc55 Splash Screen com.gearboard#24893'
2026-03-29 16:01:09.373   949-1636  BufferQueueDebug        surfaceflinger                       E  [Splash Screen com.gearboard#24895](this:0xb4000073dbe2ea80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'Splash Screen com.gearboard#24895'
2026-03-29 16:01:09.377   949-979   BufferQueueDebug        surfaceflinger                       E  [9672712 ActivityRecordInputSink com.gearboard/.MainActivity#24897](this:0xb4000073d6cc7a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from '9672712 ActivityRecordInputSink com.gearboard/.MainActivity#24897'
2026-03-29 16:01:09.396  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8210   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 16:01:09.406  3564-3564  Zygote                  com.gearboard                        E  process_name_ptr:3564 com.gearboard
2026-03-29 16:01:09.412  3564-3564  com.gearboard           com.gearboard                        I  Late-enabling -Xcheck:jni
2026-03-29 16:01:09.415  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8210   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 16:01:09.438  3564-3564  com.gearboard           com.gearboard                        I  Using CollectorTypeCC GC.
2026-03-29 16:01:09.451  3564-3564  nativeloader            com.gearboard                        D  Load libframework-connectivity-tiramisu-jni.so using APEX ns com_android_tethering for caller /apex/com.android.tethering/javalib/framework-connectivity-t.jar: ok
2026-03-29 16:01:09.454  3564-3564  MessageMonitor          com.gearboard                        I  Load libmiui_runtime
2026-03-29 16:01:09.458  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8210   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 16:01:09.469  3564-3564  re-initialized>         com.gearboard                        W  type=1400 audit(0.0:465502): avc:  granted  { execute } for  path="/data/data/com.gearboard/code_cache/startup_agents/a82ed9e3-agent.so" dev="dm-52" ino=1234699 scontext=u:r:untrusted_app:s0:c194,c257,c512,c768 tcontext=u:object_r:app_data_file:s0:c194,c257,c512,c768 tclass=file app=com.gearboard
2026-03-29 16:01:09.466  3564-3564  HighMemoryManager       com.gearboard                        I  High memory monitor not enabled
2026-03-29 16:01:09.470  3564-3576  AppScoutStateMachine    com.gearboard                        D  3564-ScoutStateMachinecreated
2026-03-29 16:01:09.470  3564-3564  nativeloader            com.gearboard                        D  Load /data/user/0/com.gearboard/code_cache/startup_agents/a82ed9e3-agent.so using system ns (caller=<unknown>): ok
2026-03-29 16:01:09.472  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8210   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 16:01:09.474  3564-3564  com.gearboard           com.gearboard                        W  hiddenapi: DexFile /data/data/com.gearboard/code_cache/.studio/instruments-2fd5708c.jar is in boot class path but is not in a known location
2026-03-29 16:01:09.617  3564-3564  com.gearboard           com.gearboard                        W  Redefining intrinsic method java.lang.Thread java.lang.Thread.currentThread(). This may cause the unexpected use of the original definition of java.lang.Thread java.lang.Thread.currentThread()in methods that have already been compiled.
2026-03-29 16:01:09.617  3564-3564  com.gearboard           com.gearboard                        W  Redefining intrinsic method boolean java.lang.Thread.interrupted(). This may cause the unexpected use of the original definition of boolean java.lang.Thread.interrupted()in methods that have already been compiled.
2026-03-29 16:01:09.622  3564-3564  FileUtils               com.gearboard                        E  err open mi_exception_log errno=2
2026-03-29 16:01:09.622  3564-3564  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:09.624  3564-3564  Compatibil...geReporter com.gearboard                        D  Compat change id reported: 171979766; UID 10450; state: ENABLED
2026-03-29 16:01:09.624  3564-3564  Compatibil...geReporter com.gearboard                        D  Compat change id reported: 242716250; UID 10450; state: ENABLED
2026-03-29 16:01:09.828  3564-3564  nativeloader            com.gearboard                        D  Configuring clns-4 for other apk /data/app/~~LCLY2zNU0lRw0KkoCMVD2w==/com.gearboard-ukJRrf-v1ZfZwnwt6bTpQQ==/base.apk. target_sdk_version=34, uses_libraries=, library_path=/data/app/~~LCLY2zNU0lRw0KkoCMVD2w==/com.gearboard-ukJRrf-v1ZfZwnwt6bTpQQ==/lib/arm64, permitted_path=/data:/mnt/expand:/data/user/0/com.gearboard
2026-03-29 16:01:09.837  3564-3564  nativeloader            com.gearboard                        D  Load libframework-connectivity-jni.so using APEX ns com_android_tethering for caller /apex/com.android.tethering/javalib/framework-connectivity.jar: ok
2026-03-29 16:01:09.840  3564-3564  GraphicsEnvironment     com.gearboard                        V  Currently set values for:
2026-03-29 16:01:09.840  3564-3564  GraphicsEnvironment     com.gearboard                        V    angle_gl_driver_selection_pkgs=[]
2026-03-29 16:01:09.840  3564-3564  GraphicsEnvironment     com.gearboard                        V    angle_gl_driver_selection_values=[]
2026-03-29 16:01:09.840  3564-3564  GraphicsEnvironment     com.gearboard                        V  ANGLE GameManagerService for com.gearboard: false
2026-03-29 16:01:09.840  3564-3564  GraphicsEnvironment     com.gearboard                        V  com.gearboard is not listed in per-application setting
2026-03-29 16:01:09.841  3564-3564  GraphicsEnvironment     com.gearboard                        V  App is not on the allowlist for updatable production driver.
2026-03-29 16:01:09.846  3564-3564  ForceDarkHelperStubImpl com.gearboard                        I  initialize for com.gearboard , ForceDarkAppConfig: null
2026-03-29 16:01:09.848  3564-3564  nativeloader            com.gearboard                        D  Load libforcedarkimpl.so using system ns (caller=/system_ext/framework/miui-framework.jar): ok
2026-03-29 16:01:09.848  3564-3564  OpenGLRenderer          com.gearboard                        D  JNI_OnLoad success
2026-03-29 16:01:09.848  3564-3564  MiuiForceDarkConfig     com.gearboard                        I  setConfig density:2.750000, mainRule:0, secondaryRule:0, tertiaryRule:0
2026-03-29 16:01:09.853  3564-3564  Compatibil...geReporter com.gearboard                        D  Compat change id reported: 183155436; UID 10450; state: ENABLED
2026-03-29 16:01:09.870  3564-3564  MbrainDebugManagerImpl  com.gearboard                        D  getService failed
2026-03-29 16:01:09.883  3564-3564  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:09.889  3564-3595  libc                    com.gearboard                        W  Access denied finding property "vendor.migl.debug"
2026-03-29 16:01:09.889  3564-3595  libMiGL                 com.gearboard                        I  EnableDR: 0
2026-03-29 16:01:09.890  3564-3595  libMEOW                 com.gearboard                        D  meow new tls: 0xb40000739afd8080
2026-03-29 16:01:09.890  3564-3595  libMEOW                 com.gearboard                        D  meow reload base cfg path: na
2026-03-29 16:01:09.890  3564-3595  libMEOW                 com.gearboard                        D  meow reload overlay cfg path: na
2026-03-29 16:01:09.890  3564-3595  QT                      com.gearboard                        W  qt_process_init() called
2026-03-29 16:01:09.890  3564-3595  QT                      com.gearboard                        E  [QT]file does not exist
2026-03-29 16:01:09.890  3564-3595  QT                      com.gearboard                        W  Support!!
2026-03-29 16:01:09.890  3564-3595  QT                      com.gearboard                        E  [QT]file does not exist
2026-03-29 16:01:09.890  3564-3595  libMEOW                 com.gearboard                        D  applied 1 plugins for [com.gearboard]:
2026-03-29 16:01:09.890  3564-3595  libMEOW                 com.gearboard                        D    plugin 1: [libMEOW_gift.so]: 0xb40000739af69480
2026-03-29 16:01:09.890  3564-3595  libMEOW                 com.gearboard                        D  rebuild call chain: 0xb40000739afe7f00
2026-03-29 16:01:09.890  3564-3595  libMEOW                 com.gearboard                        D  meow delete tls: 0xb40000739afd8080
2026-03-29 16:01:09.941  3564-3564  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.df.effect.conflict"
2026-03-29 16:01:09.937  3564-3564  com.gearboard           com.gearboard                        W  type=1400 audit(0.0:465503): avc:  denied  { read } for  name="u:object_r:vendor_displayfeature_prop:s0" dev="tmpfs" ino=10046 scontext=u:r:untrusted_app:s0:c194,c257,c512,c768 tcontext=u:object_r:vendor_displayfeature_prop:s0 tclass=file permissive=0 app=com.gearboard
2026-03-29 16:01:10.033  3564-3564  M-ProMotion             com.gearboard                        I  M-ProMotion is disabled
2026-03-29 16:01:10.035  3564-3602  nativeloader            com.gearboard                        D  Load libpowerhalwrap_jni.so using system ns (caller=/system_ext/framework/mediatek-framework.jar): ok
2026-03-29 16:01:10.036  3564-3602  PowerHalWrapper         com.gearboard                        I  PowerHalWrapper.getInstance
2026-03-29 16:01:10.036  3564-3564  SurfaceFactory          com.gearboard                        I  [static] sSurfaceFactory = com.mediatek.view.impl.SurfaceFactoryImpl@3e7a17c
2026-03-29 16:01:10.039  3564-3564  Compatibil...geReporter com.gearboard                        D  Compat change id reported: 237531167; UID 10450; state: DISABLED
2026-03-29 16:01:10.042  3564-3564  FramePredict            com.gearboard                        D  FramePredict init: false
2026-03-29 16:01:10.043  3564-3564  FramePredict            com.gearboard                        D  init success
2026-03-29 16:01:10.043  3564-3564  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.perf.scroll_opt"
2026-03-29 16:01:10.045  3564-3603  FramePredict            com.gearboard                        E  registerContentObserver fail
2026-03-29 16:01:10.047  3564-3593  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.display.iris_x7.support"
2026-03-29 16:01:10.050  3564-3564  ForceDarkHelperStubImpl com.gearboard                        I  setViewRootImplForceDark: false for com.gearboard.MainActivity@2b3306f, reason: AppDarkModeEnable
2026-03-29 16:01:10.050  3564-3564  VRI[MainActivity]       com.gearboard                        D  hardware acceleration = true, forceHwAccelerated = false
2026-03-29 16:01:10.054   949-1636  BufferQueueDebug        surfaceflinger                       E  [9cb1ab3 com.gearboard/com.gearboard.MainActivity#24903](this:0xb4000073d6902a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from '9cb1ab3 com.gearboard/com.gearboard.MainActivity#24903'
2026-03-29 16:01:10.057  3564-3564  libMEOW                 com.gearboard                        D  meow new tls: 0xb40000739b046280
2026-03-29 16:01:10.057  3564-3564  libMEOW                 com.gearboard                        D  applied 1 plugins for [com.gearboard]:
2026-03-29 16:01:10.057  3564-3564  libMEOW                 com.gearboard                        D    plugin 1: [libMEOW_gift.so]: 0xb40000739af69480
2026-03-29 16:01:10.057  3564-3564  libMEOW                 com.gearboard                        D  rebuild call chain: 0xb40000739b055200
2026-03-29 16:01:10.059  3564-3564  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:10.059  3564-3564  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:10.061  3564-3564  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:10.225  3564-3564  Compatibil...geReporter com.gearboard                        D  Compat change id reported: 263076149; UID 10450; state: ENABLED
2026-03-29 16:01:10.234  3564-3564  GearBoardMidi           com.gearboard                        D  Found 1 MIDI devices
2026-03-29 16:01:10.235  3564-3564  GearBoardMidi           com.gearboard                        D  Found 1 MIDI devices
2026-03-29 16:01:10.339  3564-3568  com.gearboard           com.gearboard                        I  Compiler allocated 6236KB to compile void android.view.ViewRootImpl.performTraversals()
2026-03-29 16:01:10.420  3564-3564  com.gearboard           com.gearboard                        W  Method boolean androidx.compose.runtime.snapshots.SnapshotStateList.conditionalUpdate(boolean, kotlin.jvm.functions.Function1) failed lock verification and will run slower.
Common causes for lock verification issues are non-optimized dex code
and incorrect proguard optimizations.
2026-03-29 16:01:10.420  3564-3564  com.gearboard           com.gearboard                        W  Method boolean androidx.compose.runtime.snapshots.SnapshotStateList.conditionalUpdate$default(androidx.compose.runtime.snapshots.SnapshotStateList, boolean, kotlin.jvm.functions.Function1, int, java.lang.Object) failed lock verification and will run slower.
2026-03-29 16:01:10.420  3564-3564  com.gearboard           com.gearboard                        W  Method java.lang.Object androidx.compose.runtime.snapshots.SnapshotStateList.mutate(kotlin.jvm.functions.Function1) failed lock verification and will run slower.
2026-03-29 16:01:10.420  3564-3564  com.gearboard           com.gearboard                        W  Method void androidx.compose.runtime.snapshots.SnapshotStateList.update(boolean, kotlin.jvm.functions.Function1) failed lock verification and will run slower.
2026-03-29 16:01:10.420  3564-3564  com.gearboard           com.gearboard                        W  Method void androidx.compose.runtime.snapshots.SnapshotStateList.update$default(androidx.compose.runtime.snapshots.SnapshotStateList, boolean, kotlin.jvm.functions.Function1, int, java.lang.Object) failed lock verification and will run slower.
2026-03-29 16:01:10.457  3564-3564  GearBoardMidi           com.gearboard                        D  Found 1 MIDI devices
2026-03-29 16:01:10.521  3564-3564  Compatibil...geReporter com.gearboard                        D  Compat change id reported: 171228096; UID 10450; state: ENABLED
2026-03-29 16:01:10.680  3564-3564  View                    com.gearboard                        D  [ANR Warning]onMeasure time too long, this =androidx.compose.ui.platform.AndroidComposeView{677135f VFED..... ......I. 0,0-0,0}time =409 ms
2026-03-29 16:01:10.680  3564-3564  View                    com.gearboard                        D  [ANR Warning]onMeasure time too long, this =androidx.compose.ui.platform.ComposeView{1832c5d V.E...... ......I. 0,0-0,0}time =410 ms
2026-03-29 16:01:10.680  3564-3564  View                    com.gearboard                        D  [ANR Warning]onMeasure time too long, this =android.widget.FrameLayout{94c69d2 V.E...... ......I. 0,0-0,0 #1020002 android:id/content}time =410 ms
2026-03-29 16:01:10.680  3564-3564  View                    com.gearboard                        D  [ANR Warning]onMeasure time too long, this =android.widget.LinearLayout{8333aa3 V.E...... ......I. 0,0-0,0}time =410 ms
2026-03-29 16:01:10.680  3564-3564  View                    com.gearboard                        D  [ANR Warning]onMeasure time too long, this =DecorView@6bfd1a0[MainActivity]time =410 ms
2026-03-29 16:01:10.681   949-1635  BufferQueueDebug        surfaceflinger                       E  [com.gearboard/com.gearboard.MainActivity#24904](this:0xb4000073d6c33a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'com.gearboard/com.gearboard.MainActivity#24904'
2026-03-29 16:01:10.688  3564-3564  BufferQueueConsumer     com.gearboard                        D  [](id:dec00000000,api:0,p:-1,c:3564) connect: controlledByApp=false
2026-03-29 16:01:10.691  3564-3593  libMEOW                 com.gearboard                        D  meow new tls: 0xb4000073345b6640
2026-03-29 16:01:10.691  3564-3593  libMEOW                 com.gearboard                        D  applied 1 plugins for [com.gearboard]:
2026-03-29 16:01:10.691  3564-3593  libMEOW                 com.gearboard                        D    plugin 1: [libMEOW_gift.so]: 0xb40000739af69480
2026-03-29 16:01:10.691  3564-3593  libMEOW                 com.gearboard                        D  rebuild call chain: 0xb4000073345c5bc0
2026-03-29 16:01:10.713  3564-3593  OpenGLRenderer          com.gearboard                        E  Unable to match the desired swap behavior.
2026-03-29 16:01:10.748  3564-3564  VRI[MainActivity]       com.gearboard                        D  vri.reportNextDraw android.view.ViewRootImpl.performTraversals:4460 android.view.ViewRootImpl.doTraversal:3069 android.view.ViewRootImpl$TraversalRunnable.run:10654 android.view.Choreographer$CallbackRecord.run:1768 android.view.Choreographer$CallbackRecord.run:1777
2026-03-29 16:01:10.748  3564-3564  VRI[MainActivity]       com.gearboard                        D  vri.Setup new sync=wmsSync-VRI[MainActivity]#0
2026-03-29 16:01:10.793  3564-3593  OpenGLRenderer          com.gearboard                        D  makeCurrent grContext:0xb40000733473b4c0 reset mTextureAvailable
2026-03-29 16:01:10.794  3564-3593  com.gearboard           com.gearboard                        D  MiuiProcessManagerServiceStub setSchedFifo
2026-03-29 16:01:10.794  3564-3593  MiuiProcessManagerImpl  com.gearboard                        I  setSchedFifo pid:3564, mode:3
2026-03-29 16:01:10.795  3564-3593  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.magt.mtk_magt_support"
2026-03-29 16:01:10.795  3564-3593  MAGT_SYNC_FRAME         com.gearboard                        D  MAGT Sync: MAGT is not supported. Disabling Sync.
2026-03-29 16:01:10.795  3564-3593  hw-ProcessState         com.gearboard                        D  Binder ioctl to enable oneway spam detection failed: Invalid argument
2026-03-29 16:01:10.797  3564-3593  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.display.iris_x7.support"
2026-03-29 16:01:10.846  3564-3593  LB                      com.gearboard                        E  fail to open file: No such file or directory
2026-03-29 16:01:10.846  3564-3593  LB                      com.gearboard                        E  fail to open node: No such file or directory
2026-03-29 16:01:10.846  3564-3593  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.display.iris_x7.support"
2026-03-29 16:01:10.846  3564-3593  BLASTBufferQueue        com.gearboard                        D  [VRI[MainActivity]#0](f:0,a:1) acquireNextBufferLocked size=1080x2400 mFrameNumber=1 applyTransaction=true mTimestamp=174587401311328(auto) mPendingTransactions.size=0 graphicBufferId=15307263442947 transform=0
2026-03-29 16:01:10.848  3564-3564  VRI[MainActivity]       com.gearboard                        D  vri.reportDrawFinished
2026-03-29 16:01:10.848  3564-3564  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:10.848  3564-3564  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:10.848  3564-3564  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:10.850   949-980   BufferQueueDebug        surfaceflinger                       E  [Surface(name=9cb1ab3 com.gearboard/com.gearboard.MainActivity#24903)/@0x17fb7e9 - animation-leash of starting_reveal#24907](this:0xb4000073dbcd7a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'Surface(name=9cb1ab3 com.gearboard/com.gearboard.MainActivity#24903)/@0x17fb7e9 - animation-leash of starting_reveal#24907'
2026-03-29 16:01:10.855  3564-3576  OpenGLRenderer          com.gearboard                        I  Davey! duration=960ms; Flags=1, FrameTimelineVsyncId=85682689, IntendedVsync=174586442550850, Vsync=174586607845180, InputEventId=0, HandleInputStart=174586617860559, AnimationStart=174586617869174, PerformTraversalsStart=174586618071328, DrawStart=174587304332790, FrameDeadline=174586462550850, FrameInterval=174586617361174, FrameStartTime=16529433, FlingPosition=0, SyncQueued=174587348668251, SyncStart=174587348818943, IssueDrawCommandsStart=174587350150713, SwapBuffers=174587399519943, FrameCompleted=174587403500174, DequeueBufferDuration=17538, QueueBufferDuration=394846, GpuCompleted=174587403500174, SwapBuffersCompleted=174587401862713, DisplayPresentTime=0, CommandSubmissionCompleted=174587399519943,
2026-03-29 16:01:10.866   949-980   BufferQueueDebug        surfaceflinger                       E  [Surface(name=c15fc55 Splash Screen com.gearboard#24893)/@0xa75b75b - animation-leash of window_animation#24908](this:0xb4000073dbcfca80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'Surface(name=c15fc55 Splash Screen com.gearboard#24893)/@0xa75b75b - animation-leash of window_animation#24908'
2026-03-29 16:01:10.872  3564-3564  Choreographer           com.gearboard                        I  Skipped 47 frames!  The application may be doing too much work on its main thread.
2026-03-29 16:01:10.970  3564-3593  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.display.iris_x7.support"
2026-03-29 16:01:10.970  3564-3564  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:10.970  3564-3564  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:10.974  3564-3564  HandWritingStubImpl     com.gearboard                        I  refreshLastKeyboardType: 1
2026-03-29 16:01:10.974  3564-3564  HandWritingStubImpl     com.gearboard                        I  getCurrentKeyboardType: 1
2026-03-29 16:01:10.987  3564-3577  OpenGLRenderer          com.gearboard                        I  Davey! duration=890ms; Flags=0, FrameTimelineVsyncId=85682981, IntendedVsync=174586640903145, Vsync=174587417784616, InputEventId=0, HandleInputStart=174587427289482, AnimationStart=174587427291020, PerformTraversalsStart=174587514115867, DrawStart=174587514267943, FrameDeadline=174587437787397, FrameInterval=174587427062636, FrameStartTime=16529393, FlingPosition=0, SyncQueued=174587525108251, SyncStart=174587525162636, IssueDrawCommandsStart=174587525354636, SwapBuffers=174587527756559, FrameCompleted=174587531930405, DequeueBufferDuration=12307, QueueBufferDuration=162847, GpuCompleted=174587531930405, SwapBuffersCompleted=174587528809405, DisplayPresentTime=0, CommandSubmissionCompleted=174587527756559,
2026-03-29 16:01:11.039  3564-3564  HandWritingStubImpl     com.gearboard                        I  getCurrentKeyboardType: 1
2026-03-29 16:01:11.335  3564-3564  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:11.338  3564-3593  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.display.iris_x7.support"
2026-03-29 16:01:11.367  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:11.407  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:11.411  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:15.377  3564-3725  ProfileInstaller        com.gearboard                        D  Installing profile for com.gearboard
2026-03-29 16:01:28.235  3564-3564  MiuiMultiWindowUtils    com.gearboard                        D  freeform resolution args raw data:{  "wide_default":{    "freeform_args": {        "vertical_portrait":{"aspect_ratio":0.626, "original_ratio":0.5643,"original_scale":0.74,"top_margin":0.168,"left_margin":0.484},        "horizontal_portrait":{"aspect_ratio":0.626, "original_ratio":0.5643,"original_scale":0.74,"top_margin":0.1222,"left_margin":0.59745},        "vertical_landscape":{"aspect_ratio":1.6, "original_ratio":1,"original_scale":0.604,"top_margin":0.2596,"left_margin":0.2624},        "horizontal_landscape":{"aspect_ratio":1.6, "original_ratio":1,"original_scale":0.604,"top_margin":0.213,"left_margin":0.3758}    },    "mini_freeform_args":{        "vertical_portrait":{"original_ratio":0.147},        "horizontal_portrait":{"original_ratio":0.147},        "vertical_landscape":{"original_ratio":0.165},        "horizontal_landscape":{"original_ratio":0.165}    }  },  "narrow_default": {    "freeform_args": {        "vertical_portrait":{"aspect_ratio":0.626, "original_ratio":1,"original_scale":0.74,"top_margin":0.0753,"left_margin":-1},        "horizontal_portrait":{"aspect_ratio":0.626, "original_ratio":1,"original_scale":0.5756,"top_margin":-1,"left_margin":0.0753},        "vertical_landscape":{"aspect_ratio":1.6, "original_ratio":0.6847,"original_scale":0.587,"top_margin":0.0753,"left_margin":-1},        "horizontal_landscape":{"aspect_ratio":1.6, "original_ratio":0.6847,"original_scale":0.587,"top_margin":-1,"left_margin":0.0753}    },    "mini_freeform_args":{        "vertical_portrait":{"original_ratio":0.26},        "horizontal_portrait":{"original_ratio":0.26},        "vertical_landscape":{"original_ratio":0.293},        "horizontal_landscape":{"original_ratio":0.293}    }  },  "regular_default": {    "freeform_args": {      "vertical_portrait":{"aspect_ratio":0.625, "original_ratio":1,"original_scale":0.7,"top_margin":0.109,"left_margin":-1},      "horizontal_portrait":{"aspect_ratio":0.6667, "original_ratio":1,"original_scale":0.6102,"top_margin":-1,"left_margin":0.026},      "vertical_landscape":{"aspect_ratio":1.6, "original_ratio":1,"original_scale":0.4244,"top_margin":0.109,"left_margin":-1},      "horizontal_landscape":{"aspect_ratio":1.6, "original_ratio":1,"original_scale":0.4244,"top_margin":-1,"left_margin":0.026}    },    "mini_freeform_args":{      "vertical_portrait":{"original_ratio":0.25},      "horizontal_portrait":{"original_ratio":0.25},      "vertical_landscape":{"original_ratio":0.25},      "horizontal_landscape":{"original_ratio":0.25}    }  },  "pad_default": {    "freeform_args": {      "vertical_portrait":{"aspect_ratio":0.5625, "original_ratio":0.375,"original_scale":0.835,"top_margin":0.049,"left_margin":0.2775},      "horizontal_portrait":{"aspect_ratio":0.5625, "original_ratio":0.375,"original_scale":0.835,"top_margin":-1,"left_margin":0.6525},      "vertical_landscape":{"aspect_ratio":-1, "original_ratio":1,"original_scale":0.468,"top_margin":0.049,"left_margin":-1},      "horizontal_landscape":{"aspect_ratio":-1, "original_ratio":1,"original_scale":0.468,"top_margin":-1,"left_margin":0.4976}    },    "mini_freeform_args":{      "vertical_portrait":{"original_ratio":0.144},      "horizontal_portrait":{"original_ratio":0.144},      "vertical_landscape":{"original_ratio":0.2},      "horizontal_landscape":{"original_ratio":0.2}    }  }}
2026-03-29 16:01:28.239  3564-3564  MiuiMultiWindowUtils    com.gearboard                        D  initFreeFormResolutionArgs failed, device is rubypro
2026-03-29 16:01:28.239  3564-3564  IS_CTS_MODE             com.gearboard                        D  false
2026-03-29 16:01:28.239  3564-3564  MULTI_WIND...CH_ENABLED com.gearboard                        D  false
2026-03-29 16:01:28.311  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.362  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.378  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.403  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.416  3564-3564  ScrollIdentify          com.gearboard                        I  on fling
2026-03-29 16:01:28.428  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.443  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.479  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.485  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.491  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.508  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.524  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.541  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.559  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.575  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.592  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.608  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.625  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.676  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.693  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.716  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.725  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.742  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.762  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.780  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.796  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.812  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.826  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.845  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.858  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.876  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.893  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.910  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.917  3564-3564  MiInputConsumer         com.gearboard                        I  optimized resample latency: 3460182 ns
2026-03-29 16:01:28.928  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.939  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.959  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.974  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:28.990  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.007  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.025  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.041  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.057  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.074  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.090  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.106  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.140  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.189  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.239  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.272  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.289  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.306  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.322  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.339  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.356  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.371  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.388  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.405  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.421  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.437  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.454  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.457  3564-3564  ScrollIdentify          com.gearboard                        I  on fling
2026-03-29 16:01:29.470  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.484  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.500  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.518  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.533  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.551  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.567  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.584  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.601  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.617  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.634  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.650  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.689  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.714  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.868  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.883  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.900  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.916  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.939  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.945  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.961  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.978  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:29.995  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.012  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.028  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.044  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.061  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.078  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.094  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.111  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.127  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.143  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.160  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.176  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.193  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.209  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.227  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.242  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.259  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.276  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.292  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.308  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.327  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.342  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.360  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.374  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.392  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.408  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.425  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.442  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.456  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.474  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.491  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.507  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.524  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.540  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.557  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.572  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.590  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.607  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:30.623  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.275  3564-3593  libc                    com.gearboard                        W  Access denied finding property "ro.vendor.display.iris_x7.support"
2026-03-29 16:01:31.276  3564-3564  ForceDarkHelperStubImpl com.gearboard                        I  setViewRootImplForceDark: false for com.gearboard.MainActivity@2b3306f, reason: AppDarkModeEnable
2026-03-29 16:01:31.276  3564-3564  VRI[Спливаюче вікно]    com.gearboard                        D  hardware acceleration = true, forceHwAccelerated = false
2026-03-29 16:01:31.319  3564-3564  WindowOnBackDispatcher  com.gearboard                        W  OnBackInvokedCallback is not enabled for the application.
Set 'android:enableOnBackInvokedCallback="true"' in the application manifest.
2026-03-29 16:01:31.319  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.336  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.352  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.368  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.383  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.401  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.418  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.423  3564-3564  BufferQueueConsumer     com.gearboard                        D  [](id:dec00000001,api:0,p:-1,c:3564) connect: controlledByApp=false
2026-03-29 16:01:31.424  3564-3564  ForceDarkHelperStubImpl com.gearboard                        I  setViewRootImplForceDark: false for com.gearboard.MainActivity@2b3306f, reason: AppDarkModeEnable
2026-03-29 16:01:31.425  3564-3593  OpenGLRenderer          com.gearboard                        E  Unable to match the desired swap behavior.
2026-03-29 16:01:31.436  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.439  3564-3564  VRI[Спливаюче вікно]    com.gearboard                        D  vri.reportNextDraw android.view.ViewRootImpl.performTraversals:4460 android.view.ViewRootImpl.doTraversal:3069 android.view.ViewRootImpl$TraversalRunnable.run:10654 android.view.Choreographer$CallbackRecord.run:1768 android.view.Choreographer$CallbackRecord.run:1777
2026-03-29 16:01:31.439  3564-3564  VRI[Спливаюче вікно]    com.gearboard                        D  vri.Setup new sync=wmsSync-VRI[Спливаюче вікно]#2
2026-03-29 16:01:31.450  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.454  3564-3593  OpenGLRenderer          com.gearboard                        D  makeCurrent grContext:0xb40000733473b4c0 reset mTextureAvailable
2026-03-29 16:01:31.458  3564-3593  BLASTBufferQueue        com.gearboard                        D  [VRI[Спливаюче вікно]#1](f:0,a:1) acquireNextBufferLocked size=1080x2400 mFrameNumber=1 applyTransaction=true mTimestamp=174608012867637(auto) mPendingTransactions.size=0 graphicBufferId=15307263442951 transform=0
2026-03-29 16:01:31.459  3564-3564  VRI[Спливаюче вікно]    com.gearboard                        D  vri.reportDrawFinished
2026-03-29 16:01:31.459  3564-3564  FileUtils               com.gearboard                        E  err write to mi_exception_log
2026-03-29 16:01:31.466  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.483  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.484  3564-3564  VRI[Спливаюче вікно]    com.gearboard                        D  vri.reportNextDraw android.view.ViewRootImpl.handleResized:2368 android.view.ViewRootImpl.-$$Nest$mhandleResized:0 android.view.ViewRootImpl$ViewRootHandler.handleMessageImpl:6889 android.view.ViewRootImpl$ViewRootHandler.handleMessage:6858 android.os.Handler.dispatchMessage:106
2026-03-29 16:01:31.504  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.505  3564-3564  VRI[Спливаюче вікно]    com.gearboard                        D  vri.Setup new sync=wmsSync-VRI[Спливаюче вікно]#4
2026-03-29 16:01:31.514  3564-3564  VRI[Спливаюче вікно]    com.gearboard                        D  vri.reportDrawFinished
2026-03-29 16:01:31.514  3564-3564  HandWritingStubImpl     com.gearboard                        I  refreshLastKeyboardType: 1
2026-03-29 16:01:31.515  3564-3564  HandWritingStubImpl     com.gearboard                        I  getCurrentKeyboardType: 1
2026-03-29 16:01:31.516  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.538  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.547  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.564  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.568  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.581  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.597  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.599  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.614  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.616  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.629  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.631  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.649  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.652  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.667  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.670  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.681  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.683  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.695  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.700  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.711  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.713  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.732  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.733  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.748  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.751  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.766  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.768  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.782  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.785  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.797  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.801  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.812  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.816  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.832  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.834  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.847  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.851  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.862  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.865  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.879  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.883  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.899  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.902  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.912  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.931  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.947  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.964  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.980  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:31.996  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:32.014  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:32.030  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:32.047  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:32.062  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:32.077  3564-3593  gralloc4                com.gearboard                        I  @set_metadata: update dataspace from GM (0x00000000 -> 0x08010000)
2026-03-29 16:01:34.709  3564-3564  WindowOnBackDispatcher  com.gearboard                        W  sendCancelIfRunning: isInProgress=falsecallback=android.view.ViewRootImpl$$ExternalSyntheticLambda23@b12d61b
2026-03-29 16:01:34.712  3564-3564  View                    com.gearboard                        D  [Warning] assignParent to null: this = androidx.compose.material3.ModalBottomSheetWindow{199b6d V.E...... ......ID 0,0-1080,2400 #1020002 android:id/content aid=1073741824}
2026-03-29 16:01:34.712  3564-3564  BLASTBufferQueue        com.gearboard                        D  [VRI[Спливаюче вікно]#1](f:0,a:1) destructor()
2026-03-29 16:01:34.712  3564-3564  BufferQueueConsumer     com.gearboard                        D  [VRI[Спливаюче вікно]#1(BLAST Consumer)1](id:dec00000001,api:0,p:-1,c:3564) disconnect
2026-03-29 16:01:34.767  3564-3564  AndroidRuntime          com.gearboard                        D  Shutting down VM
2026-03-29 16:01:34.770  3564-3564  AndroidRuntime          com.gearboard                        E  FATAL EXCEPTION: main
Process: com.gearboard, PID: 3564
java.lang.IllegalArgumentException: LazyVerticalGrid's width should be bound by parent.
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke-0kLqBqw(LazyGridDsl.kt:155)
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke(LazyGridDsl.kt:154)
at androidx.compose.foundation.lazy.grid.GridSlotCache.invoke-0kLqBqw(LazyGridDsl.kt:235)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke-0kLqBqw(LazyGrid.kt:216)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke(LazyGrid.kt:177)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.SizeNode.measure-3p2s80s(Size.kt:838)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:251)
at androidx.compose.runtime.snapshots.Snapshot$Companion.observe(Snapshot.kt:2303)
at androidx.compose.runtime.snapshots.SnapshotStateObserver$ObservedScopeMap.observe(SnapshotStateObserver.kt:500)
at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:256)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeReads$ui_release(OwnerSnapshotObserver.kt:133)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeMeasureSnapshotReads$ui_release(OwnerSnapshotObserver.kt:113)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:1617)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.access$performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:36)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:620)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.measure-BRTryo0(LayoutNodeLayoutDelegate.kt:596)
at androidx.compose.foundation.layout.RowColumnMeasurementHelper.measureWithoutPlacing-_EkL_-Y(RowColumnMeasurementHelper.kt:112)
at androidx.compose.foundation.layout.RowColumnMeasurePolicy.measure-3p2s80s(RowColumnImpl.kt:72)
2026-03-29 16:01:34.770  3564-3564  AndroidRuntime          com.gearboard                        E  	at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.layout.PaddingNode.measure-3p2s80s(Padding.kt:397)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.SizeNode.measure-3p2s80s(Size.kt:838)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.PaddingNode.measure-3p2s80s(Padding.kt:397)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:251)
at androidx.compose.runtime.snapshots.Snapshot$Companion.observe(Snapshot.kt:2303)
at androidx.compose.runtime.snapshots.SnapshotStateObserver$ObservedScopeMap.observe(SnapshotStateObserver.kt:500)
at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:256)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeReads$ui_release(OwnerSnapshotObserver.kt:133)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeMeasureSnapshotReads$ui_release(OwnerSnapshotObserver.kt:113)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:1617)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.access$performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:36)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:620)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.measure-BRTryo0(LayoutNodeLayoutDelegate.kt:596)
at androidx.compose.foundation.lazy.layout.LazyLayoutMeasureScopeImpl.measure-0kLqBqw(LazyLayoutMeasureScope.kt:127)
at androidx.compose.foundation.lazy.LazyListMeasuredItemProvider.getAndMeasure(LazyListMeasuredItemProvider.kt:48)
at androidx.compose.foundation.lazy.LazyListMeasureKt.measureLazyList-5IMabDg(LazyListMeasure.kt:195)
at androidx.compose.foundation.lazy.LazyListKt$rememberLazyListMeasurePolicy$1$1.invoke-0kLqBqw(LazyList.kt:313)
at androidx.compose.foundation.lazy.LazyListKt$rememberLazyListMeasurePolicy$1$1.invoke(LazyList.kt:178)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
2026-03-29 16:01:34.771  3564-3564  AndroidRuntime          com.gearboard                        E  	at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:251)
at androidx.compose.runtime.snapshots.Snapshot$Companion.observe(Snapshot.kt:2303)
at androidx.compose.runtime.snapshots.SnapshotStateObserver$ObservedScopeMap.observe(SnapshotStateObserver.kt:500)
at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:256)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeReads$ui_release(OwnerSnapshotObserver.kt:133)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeMeasureSnapshotReads$ui_release(OwnerSnapshotObserver.kt:113)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:1617)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.access$performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:36)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:620)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.measure-BRTryo0(LayoutNodeLayoutDelegate.kt:596)
at androidx.compose.foundation.layout.RowColumnMeasurementHelper.measureWithoutPlacing-_EkL_-Y(RowColumnMeasurementHelper.kt:112)
at androidx.compose.foundation.layout.RowColumnMeasurePolicy.measure-3p2s80s(RowColumnImpl.kt:72)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.layout.FillNode.measure-3p2s80s(Size.kt:699)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:251)
at androidx.compose.runtime.snapshots.Snapshot$Companion.observe(Snapshot.kt:2303)
at androidx.compose.runtime.snapshots.SnapshotStateObserver$ObservedScopeMap.observe(SnapshotStateObserver.kt:500)
at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:256)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeReads$ui_release(OwnerSnapshotObserver.kt:133)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeMeasureSnapshotReads$ui_release(OwnerSnapshotObserver.kt:113)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:1617)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.access$performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:36)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:620)
at androidx.compose.ui.node.LayoutNode.remeasure-_Sx5XlM$ui_release(LayoutNode.kt:1145)
at androidx.compose.ui.node.LayoutNode.remeasure-_Sx5XlM$ui_release$default(LayoutNode.kt:1136)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.doRemeasure-sdFAvZA(MeasureAndLayoutDelegate.kt:356)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.remeasureAndRelayoutIfNeeded(MeasureAndLayoutDelegate.kt:514)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.onlyRemeasureIfScheduled(MeasureAndLayoutDelegate.kt:598)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.forceMeasureTheSubtreeInternal(MeasureAndLayoutDelegate.kt:624)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.forceMeasureTheSubtreeInternal(MeasureAndLayoutDelegate.kt:631)
2026-03-29 16:01:34.771  3564-3564  AndroidRuntime          com.gearboard                        E  	at androidx.compose.ui.node.MeasureAndLayoutDelegate.forceMeasureTheSubtreeInternal(MeasureAndLayoutDelegate.kt:631)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.forceMeasureTheSubtreeInternal(MeasureAndLayoutDelegate.kt:631)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.forceMeasureTheSubtree(MeasureAndLayoutDelegate.kt:587)
at androidx.compose.ui.platform.AndroidComposeView.forceMeasureTheSubtree(AndroidComposeView.android.kt:993)
at androidx.compose.ui.node.Owner.forceMeasureTheSubtree$default(Owner.kt:239)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:632)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.measure-BRTryo0(LayoutNodeLayoutDelegate.kt:596)
at androidx.compose.foundation.lazy.layout.LazyLayoutMeasureScopeImpl.measure-0kLqBqw(LazyLayoutMeasureScope.kt:127)
at androidx.compose.foundation.lazy.LazyListMeasuredItemProvider.getAndMeasure(LazyListMeasuredItemProvider.kt:48)
at androidx.compose.foundation.lazy.LazyListMeasureKt.measureLazyList-5IMabDg(LazyListMeasure.kt:195)
at androidx.compose.foundation.lazy.LazyListKt$rememberLazyListMeasurePolicy$1$1.invoke-0kLqBqw(LazyList.kt:313)
at androidx.compose.foundation.lazy.LazyListKt$rememberLazyListMeasurePolicy$1$1.invoke(LazyList.kt:178)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.PaddingValuesModifier.measure-3p2s80s(Padding.kt:455)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.FillNode.measure-3p2s80s(Size.kt:699)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:251)
at androidx.compose.runtime.snapshots.Snapshot$Companion.observe(Snapshot.kt:2303)
at androidx.compose.runtime.snapshots.SnapshotStateObserver$ObservedScopeMap.observe(SnapshotStateObserver.kt:500)
at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:256)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeReads$ui_release(OwnerSnapshotObserver.kt:133)
2026-03-29 16:01:34.771  3564-3564  AndroidRuntime          com.gearboard                        E  	at androidx.compose.ui.node.OwnerSnapshotObserver.observeMeasureSnapshotReads$ui_release(OwnerSnapshotObserver.kt:113)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:1617)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.access$performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:36)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:620)
at androidx.compose.ui.node.LayoutNode.remeasure-_Sx5XlM$ui_release(LayoutNode.kt:1145)
at androidx.compose.ui.node.LayoutNode.remeasure-_Sx5XlM$ui_release$default(LayoutNode.kt:1136)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.doRemeasure-sdFAvZA(MeasureAndLayoutDelegate.kt:356)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.remeasureAndRelayoutIfNeeded(MeasureAndLayoutDelegate.kt:514)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.remeasureAndRelayoutIfNeeded$default(MeasureAndLayoutDelegate.kt:491)
at androidx.compose.ui.node.MeasureAndLayoutDelegate.measureAndLayout(MeasureAndLayoutDelegate.kt:377)
at androidx.compose.ui.platform.AndroidComposeView.measureAndLayout(AndroidComposeView.android.kt:971)
at androidx.compose.ui.node.Owner.measureAndLayout$default(Owner.kt:228)
at androidx.compose.ui.platform.AndroidComposeView.dispatchDraw(AndroidComposeView.android.kt:1224)
at android.view.View.draw(View.java:25001)
at android.view.View.updateDisplayListIfDirty(View.java:23784)
at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:4668)
at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:4641)
at android.view.View.updateDisplayListIfDirty(View.java:23709)
at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:4668)
at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:4641)
at android.view.View.updateDisplayListIfDirty(View.java:23709)
at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:4668)
at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:4641)
at android.view.View.updateDisplayListIfDirty(View.java:23709)
at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:4668)
at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:4641)
at android.view.View.updateDisplayListIfDirty(View.java:23709)
at android.view.ThreadedRenderer.updateViewTreeDisplayList(ThreadedRenderer.java:736)
at android.view.ThreadedRenderer.updateRootDisplayList(ThreadedRenderer.java:745)
at android.view.ThreadedRenderer.draw(ThreadedRenderer.java:855)
at android.view.ViewRootImpl.draw(ViewRootImpl.java:5796)
at android.view.ViewRootImpl.performDraw(ViewRootImpl.java:5439)
at android.view.ViewRootImpl.performTraversals(ViewRootImpl.java:4519)
at android.view.ViewRootImpl.doTraversal(ViewRootImpl.java:3069)
at android.view.ViewRootImpl$TraversalRunnable.run(ViewRootImpl.java:10654)
at android.view.Choreographer$CallbackRecord.run(Choreographer.java:1768)
at android.view.Choreographer$CallbackRecord.run(Choreographer.java:1777)
at android.view.Choreographer.doCallbacks(Choreographer.java:1247)
at android.view.Choreographer.doFrame(Choreographer.java:1114)
at android.view.Choreographer$FrameDisplayEventReceiver.run(Choreographer.java:1731)
at android.os.Handler.handleCallback(Handler.java:958)
at android.os.Handler.dispatchMessage(Handler.java:99)
at android.os.Looper.loopOnce(Looper.java:222)
at android.os.Looper.loop(Looper.java:314)
at android.app.ActivityThread.main(ActivityThread.java:8788)
at java.lang.reflect.Method.invoke(Native Method)
at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:569)
at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1090)
2026-03-29 16:01:34.774  3564-3564  ScoutUtils              com.gearboard                        W  Failed to mkdir /data/miuilog/stability/memleak/heapdump/
2026-03-29 16:01:34.776  4743-5061  JavaExceptionHandler    com.miui.daemon                      E  Process: com.gearboard, PID: 3564
java.lang.IllegalArgumentException: LazyVerticalGrid's width should be bound by parent.
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke-0kLqBqw(LazyGridDsl.kt:155)
at androidx.compose.foundation.lazy.grid.LazyGridDslKt$rememberColumnWidthSums$1$1.invoke(LazyGridDsl.kt:154)
at androidx.compose.foundation.lazy.grid.GridSlotCache.invoke-0kLqBqw(LazyGridDsl.kt:235)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke-0kLqBqw(LazyGrid.kt:216)
at androidx.compose.foundation.lazy.grid.LazyGridKt$rememberLazyGridMeasurePolicy$1$1.invoke(LazyGrid.kt:177)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke-0kLqBqw(LazyLayout.kt:107)
at androidx.compose.foundation.lazy.layout.LazyLayoutKt$LazyLayout$3$2$1.invoke(LazyLayout.kt:100)
at androidx.compose.ui.layout.LayoutNodeSubcompositionsState$createMeasurePolicy$1.measure-3p2s80s(SubcomposeLayout.kt:709)
at androidx.compose.ui.node.InnerNodeCoordinator.measure-BRTryo0(InnerNodeCoordinator.kt:126)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke-3p2s80s(AndroidOverscroll.android.kt:584)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$2.invoke(AndroidOverscroll.android.kt:583)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke-3p2s80s(AndroidOverscroll.android.kt:568)
at androidx.compose.foundation.AndroidOverscroll_androidKt$StretchOverscrollNonClippingLayer$1.invoke(AndroidOverscroll.android.kt:567)
at androidx.compose.ui.layout.LayoutModifierImpl.measure-3p2s80s(LayoutModifier.kt:294)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.graphics.SimpleGraphicsLayerModifier.measure-3p2s80s(GraphicsLayerModifier.kt:646)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.foundation.layout.SizeNode.measure-3p2s80s(Size.kt:838)
at androidx.compose.ui.node.LayoutModifierNodeCoordinator.measure-BRTryo0(LayoutModifierNodeCoordinator.kt:116)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:252)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$performMeasureBlock$1.invoke(LayoutNodeLayoutDelegate.kt:251)
at androidx.compose.runtime.snapshots.Snapshot$Companion.observe(Snapshot.kt:2303)
at androidx.compose.runtime.snapshots.SnapshotStateObserver$ObservedScopeMap.observe(SnapshotStateObserver.kt:500)
at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:256)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeReads$ui_release(OwnerSnapshotObserver.kt:133)
at androidx.compose.ui.node.OwnerSnapshotObserver.observeMeasureSnapshotReads$ui_release(OwnerSnapshotObserver.kt:113)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:1617)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate.access$performMeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:36)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.remeasure-BRTryo0(LayoutNodeLayoutDelegate.kt:620)
at androidx.compose.ui.node.LayoutNodeLayoutDelegate$MeasurePassDelegate.measure-BRTryo0(LayoutNodeLayoutDelegate.kt:596)
at androidx.compose.foundation.layout.RowColumnMeasurementHelper.measureWithoutPlacing-_EkL_-Y(RowColumnMeasurementHelper.kt:112)
at androidx.compose.foundation.layout.RowColumnMeasurePolicy.measure-3p2s80s(RowColumnImpl.kt:72)
2026-03-29 16:01:34.812   949-980   BufferQueueDebug        surfaceflinger                       E  [360b6b4 Application Error: com.gearboard#24919](this:0xb4000073dbceda80,id:-1,api:0,p:-1,c:-1) id info cannot be read from '360b6b4 Application Error: com.gearboard#24919'
2026-03-29 16:01:34.819   949-980   BufferQueueDebug        surfaceflinger                       E  [Application Error: com.gearboard#24922](this:0xb4000073dbd75a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'Application Error: com.gearboard#24922'
2026-03-29 16:01:34.838   949-1636  BufferQueueDebug        surfaceflinger                       E  [Surface(name=360b6b4 Application Error: com.gearboard#24919)/@0xa574e52 - animation-leash of window_animation#24923](this:0xb4000073d6911a80,id:-1,api:0,p:-1,c:-1) id info cannot be read from 'Surface(name=360b6b4 Application Error: com.gearboard#24919)/@0xa574e52 - animation-leash of window_animation#24923'
2026-03-29 16:01:35.397  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8210   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 16:01:35.401  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8210   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
2026-03-29 16:01:39.791  3564-3565  com.gearboard           com.gearboard                        I  Thread[2,tid=3565,WaitingInMainSignalCatcherLoop,Thread*=0xb4000073a6800000,peer=0x26801e0,"Signal Catcher"]: reacting to signal 3
2026-03-29 16:01:39.792  3564-3565  com.gearboard           com.gearboard                        I  
2026-03-29 16:01:40.277  3564-3565  com.gearboard           com.gearboard                        I  Wrote stack traces to tombstoned
2026-03-29 16:01:46.279  2474-2775  ActivityManagerWrapper  com.miui.home                        E  getRecentTasksForceIncludingTaskIdIfValid: getRecentTasks: size=1
mainTaskId=8210   userId=0   windowMode=1   baseIntent=Intent { act=android.intent.action.MAIN flag=270532608 cmp=ComponentInfo{com.gearboard/com.gearboard.MainActivity} }
