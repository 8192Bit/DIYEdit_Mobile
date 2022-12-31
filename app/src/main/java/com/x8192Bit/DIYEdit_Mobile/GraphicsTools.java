package com.x8192Bit.DIYEdit_Mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

import androidx.appcompat.content.res.AppCompatResources;

import x8192Bit.DIYEdit_Mobile.R;

public class GraphicsTools {

    public final int YELLOW = 0;
    public final int LIGHTBLUE = 1;
    public final int GREEN = 2;
    public final int ORANGE = 3;
    public final int DARKBLUE = 4;
    public final int RED = 5;
    public final int WHITE = 6;
    public final int BLACK = 7;

    public final int RR_CIRCLE = 0;
    public final int RR_SQUARE = 1;
    public final int RR_STAR = 2;
    public final int RR_HEXAGON = 3;
    public final int RR_CLOVER = 4;
    public final int RR_DIAMOND = 5;
    public final int RR_PLUS = 6;
    public final int RR_FLOWER = 7;

    public final int RI_HEART = 0;
    public final int RI_FLOWER = 1;
    public final int RI_STORM = 2;
    public final int RI_CROSS = 3;
    public final int RI_LEAF = 4;
    public final int RI_DROPLET = 5;
    public final int RI_LIGHTNING = 6;
    public final int RI_SMILE = 7;

    public final int GI_PULSE = 0;
    public final int GI_ROCKET = 1;
    public final int GI_SHIELD = 2;
    public final int GI_PUNCH = 3;
    public final int GI_CAR = 4;
    public final int GI_BAT = 5;
    public final int GI_WHAT = 6;
    public final int GI_OTHER = 7;

    public final int GG_ROUND = 0;
    public final int GG_NOTCH = 1;
    public final int GG_FLAT = 2;
    public final int GG_RIDGES = 3;
    public final int GG_ZIGZAG = 4;
    public final int GG_PRONGS = 5;
    public final int GG_ANGLED = 6;
    public final int GG_GBA = 7;
    public final int GG_BIGNAME = 8;
    // maybe the same as com.xperia64.diyedit.metadata.gamemetadata.getCartType()

    public final int CI_LETTER = 0;
    public final int CI_HOUSE = 1;
    public final int CI_SHIELD = 2;
    public final int CI_SKULL = 3;
    public final int CI_CAT = 4;
    public final int CI_BAT = 5;
    public final int CI_SPACESHIP = 6;
    public final int CI_SMILE = 7;

    // PLEASE DO NOT DEAL WITH THESE THING ANYMORE!!!
    // I HATE THESE WORKS!!!!!!!
    public int getDIYColor(int dc) {
        switch (dc) {
            case YELLOW:
                return 0xFFFFFF5A;
            case LIGHTBLUE:
                return 0xFF10C6CE;
            case GREEN:
                return 0xFF089452;
            case ORANGE:
                return 0xFFFFAD31;
            case DARKBLUE:
                return 0xFF296BC6;
            case RED:
                return 0xFFFF0000;
            case WHITE:
                return 0xFF000000;
            case BLACK:
                return 0xFFFFFFFF;
            default:
                return -1;
        }
    }

    public class GameIcon {
        public GameIcon() {
        }

        public int getCartridgeColor() {
            return cartridgeColor;
        }

        public void setCartridgeColor(int cartridgeColor) {
            this.cartridgeColor = cartridgeColor;
        }

        public int getCartridgeShape() {
            return cartridgeShape;
        }

        public void setCartridgeShape(int cartridgeShape) {
            this.cartridgeShape = cartridgeShape;
        }

        public int getIconColor() {
            return iconColor;
        }

        public void setIconColor(int iconColor) {
            this.iconColor = iconColor;
        }

        public int getIconShape() {
            return iconShape;
        }

        public void setIconShape(int iconShape) {
            this.iconShape = iconShape;
        }

        @Deprecated
        public BitmapDrawable renderImage(Context ctx, int width, int height) {
            // the final one
            Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            // the one to draw 16x16 icons directly
            Bitmap plane = Bitmap.createBitmap(16, 16, Bitmap.Config.RGB_565);
            // common paint
            Paint p = new Paint();
            // common canvas
            Canvas c = new Canvas(plane);
            int ResID;
            // don't touch these SWITCHES.
            // they will pollute your eyes

            // THE SONG OF RENDER
            // BY POOR 8192BIT
            // get cartridge drawable
            switch (cartridgeShape) {
                case GG_ROUND:
                    ResID = R.drawable.spr_cart1;
                    break;
                case GG_NOTCH:
                    ResID = R.drawable.spr_cart2;
                    break;
                case GG_FLAT:
                    ResID = R.drawable.spr_cart3;
                    break;
                case GG_RIDGES:
                    ResID = R.drawable.spr_cart4;
                    break;
                case GG_ZIGZAG:
                    ResID = R.drawable.spr_cart5;
                    break;
                case GG_PRONGS:
                    ResID = R.drawable.spr_cart6;
                    break;
                case GG_ANGLED:
                    ResID = R.drawable.spr_cart7;
                    break;
                case GG_GBA:
                    ResID = R.drawable.spr_cart8;
                    break;
                case GG_BIGNAME:
                    ResID = R.drawable.spr_cart9;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + cartridgeShape);
            }
            // fill with color, and draw them to the buffer
            BitmapDrawable drawableToDraw = (BitmapDrawable) AppCompatResources.getDrawable(ctx, ResID);
            new Canvas(drawableToDraw.getBitmap()).drawColor(getDIYColor(cartridgeColor), PorterDuff.Mode.ADD);
            c.drawBitmap(drawableToDraw.getBitmap(), 0, 0, p);
            // get icon drawable
            switch (iconShape) {
                case GI_PULSE:
                    ResID = R.drawable.spr_cart_logo1;
                    break;
                case GI_ROCKET:
                    ResID = R.drawable.spr_cart_logo2;
                    break;
                case GI_SHIELD:
                    ResID = R.drawable.spr_cart_logo3;
                    break;
                case GI_PUNCH:
                    ResID = R.drawable.spr_cart_logo4;
                    break;
                case GI_CAR:
                    ResID = R.drawable.spr_cart_logo5;
                    break;
                case GI_BAT:
                    ResID = R.drawable.spr_cart_logo6;
                    break;
                case GI_WHAT:
                    ResID = R.drawable.spr_cart_logo7;
                    break;
                case GI_OTHER:
                    ResID = R.drawable.spr_cart_logo8;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + cartridgeShape);
            }
            drawableToDraw = (BitmapDrawable) AppCompatResources.getDrawable(ctx, ResID);
            new Canvas(drawableToDraw.getBitmap()).drawColor(getDIYColor(iconColor), PorterDuff.Mode.ADD);
            c.drawBitmap(drawableToDraw.getBitmap(), 0, 0, p);
            // fill with color, and draw them to the buffer
            // then draw the buffer to a big picture

            new Canvas(b).drawBitmap(plane,new Rect(0,0,16,16),new Rect(0,0,width,height),p);
            // SHITS DONE YO
            return new BitmapDrawable(b);
        }

        int cartridgeColor;
        int cartridgeShape;
        int iconColor;
        int iconShape;

    }
}
