package app.simple.inure.decorations.transitions;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.transition.CircularPropagation;
import androidx.transition.TransitionValues;
import androidx.transition.Visibility;
import app.simple.inure.R;

/**
 * This transition tracks changes to the visibility of target views in the
 * start and end scenes and moves views in or out from the edges of the
 * scene. Visibility is determined by both the
 * {@link View#setVisibility(int)} state of the view as well as whether it
 * is parented in the current view hierarchy. Disappearing Views are
 * limited as described in {@link Visibility#onDisappear(android.view.ViewGroup,
 * TransitionValues, int, TransitionValues, int)}.
 * <p>Views move away from the focal View or the center of the Scene if
 * no epicenter was provided.</p>
 */
public class Explode extends Visibility {
    
    private static final TimeInterpolator sDecelerate = new DecelerateInterpolator(1.5F);
    private static final TimeInterpolator sAccelerate = new DecelerateInterpolator(1.5F);
    private static final String PROPNAME_SCREEN_BOUNDS = "android:explode:screenBounds";
    
    private final int[] mTempLoc = new int[2];
    
    public Explode() {
        setPropagation(new CircularPropagation());
    }
    
    public Explode(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPropagation(new CircularPropagation());
    }
    
    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        view.getLocationOnScreen(mTempLoc);
        int left = mTempLoc[0];
        int top = mTempLoc[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();
        transitionValues.values.put(PROPNAME_SCREEN_BOUNDS, new Rect(left, top, right, bottom));
    }
    
    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        captureValues(transitionValues);
    }
    
    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        captureValues(transitionValues);
    }
    
    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view,
            TransitionValues startValues, TransitionValues endValues) {
        if (endValues == null) {
            return null;
        }
        Rect bounds = (Rect) endValues.values.get(PROPNAME_SCREEN_BOUNDS);
        float endX = view.getTranslationX();
        float endY = view.getTranslationY();
        calculateOut(sceneRoot, bounds, mTempLoc);
        float startX = endX + mTempLoc[0];
        float startY = endY + mTempLoc[1];
        
        return TranslationAnimationCreator.createAnimation(view, endValues, bounds != null ? bounds.left : 0, bounds != null ? bounds.top : 0,
                startX, startY, endX, endY, sDecelerate, this);
    }
    
    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view,
            TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null) {
            return null;
        }
        Rect bounds = (Rect) startValues.values.get(PROPNAME_SCREEN_BOUNDS);
        int viewPosX = bounds.left;
        int viewPosY = bounds.top;
        float startX = view.getTranslationX();
        float startY = view.getTranslationY();
        float endX = startX;
        float endY = startY;
        int[] interruptedPosition = (int[]) startValues.view.getTag(R.id.transition_position);
        if (interruptedPosition != null) {
            // We want to have the end position relative to the interrupted position, not
            // the position it was supposed to start at.
            endX += interruptedPosition[0] - bounds.left;
            endY += interruptedPosition[1] - bounds.top;
            bounds.offsetTo(interruptedPosition[0], interruptedPosition[1]);
        }
        calculateOut(sceneRoot, bounds, mTempLoc);
        endX += mTempLoc[0];
        endY += mTempLoc[1];
        
        return TranslationAnimationCreator.createAnimation(view, startValues,
                viewPosX, viewPosY, startX, startY, endX, endY, sAccelerate, this);
    }
    
    private void calculateOut(View sceneRoot, Rect bounds, int[] outVector) {
        sceneRoot.getLocationOnScreen(mTempLoc);
        int sceneRootX = mTempLoc[0];
        int sceneRootY = mTempLoc[1];
        int focalX;
        int focalY;
        
        Rect epicenter = getEpicenter();
        if (epicenter == null) {
            focalX = sceneRootX + (sceneRoot.getWidth() / 2)
                    + Math.round(sceneRoot.getTranslationX());
            focalY = sceneRootY + (sceneRoot.getHeight() / 2)
                    + Math.round(sceneRoot.getTranslationY());
        }
        else {
            focalX = epicenter.centerX();
            focalY = epicenter.centerY();
        }
        
        int centerX = bounds.centerX();
        int centerY = bounds.centerY();
        float xVector = centerX - focalX;
        float yVector = centerY - focalY;
        
        if (xVector == 0 && yVector == 0) {
            // Random direction when View is centered on focal View.
            xVector = (float) (Math.random() * 2) - 1;
            yVector = (float) (Math.random() * 2) - 1;
        }
        float vectorSize = calculateDistance(xVector, yVector);
        xVector /= vectorSize;
        yVector /= vectorSize;
        
        float maxDistance =
                calculateMaxDistance(sceneRoot, focalX - sceneRootX, focalY - sceneRootY);
        
        outVector[0] = Math.round(maxDistance * xVector);
        outVector[1] = Math.round(maxDistance * yVector);
    }
    
    private static float calculateMaxDistance(View sceneRoot, int focalX, int focalY) {
        int maxX = Math.max(focalX, sceneRoot.getWidth() - focalX);
        int maxY = Math.max(focalY, sceneRoot.getHeight() - focalY);
        return calculateDistance(maxX, maxY);
    }
    
    private static float calculateDistance(float x, float y) {
        return (float) Math.sqrt((x * x) + (y * y));
    }
    
}

