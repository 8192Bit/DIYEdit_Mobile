package com.x8192Bit.DIYEdit_Mobile.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

import androidx.appcompat.content.res.AppCompatResources;

import x8192Bit.DIYEdit_Mobile.R;

public class GraphicsUtils {

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

    // PLEASE DO NOT DEAL WITH THESE THINGS ANYMORE!!!
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
                return 0xFFFFFFFF;
            case BLACK:
                return 0xFF000000;
            default:
                return -1;
        }
    }

    // feel better with CrystalDiskInfo.
    // strongly recommend
    public int getDIYSecondaryColor(int dc) {
        switch (dc) {
            case YELLOW:
                return 0xFFF7F752;
            case LIGHTBLUE:
                return 0xFF84E7EF;
            case GREEN:
                return 0xFF10CE73;
            case ORANGE:
                return 0xFFEF9C21;
            case DARKBLUE:
                return 0xFF4A94EF;
            case RED:
                return 0xFFFF7373;
            case WHITE:
                return 0xFFFFFFFF;
            case BLACK:
                return 0xFF737373;
            default:
                return -1;
        }
    }

    public int getCartridgeIconID(boolean isReversed, int iconShape) {

        if (isReversed) {
            switch (iconShape) {
                case GI_PULSE:
                    return R.drawable.spr_cart_logo1_r;
                case GI_ROCKET:
                    return R.drawable.spr_cart_logo2_r;
                case GI_SHIELD:
                    return R.drawable.spr_cart_logo3_r;
                case GI_PUNCH:
                    return R.drawable.spr_cart_logo4_r;
                case GI_CAR:
                    return R.drawable.spr_cart_logo5_r;
                case GI_BAT:
                    return R.drawable.spr_cart_logo6_r;
                case GI_WHAT:
                    return R.drawable.spr_cart_logo7_r;
                case GI_OTHER:
                    return R.drawable.spr_cart_logo8_r;
                default:
                    throw new IllegalStateException("Unexpected value: " + iconShape);
            }
        } else {
            switch (iconShape) {
                case GI_PULSE:
                    return R.drawable.spr_cart_logo1;
                case GI_ROCKET:
                    return R.drawable.spr_cart_logo2;
                case GI_SHIELD:
                    return R.drawable.spr_cart_logo3;
                case GI_PUNCH:
                    return R.drawable.spr_cart_logo4;
                case GI_CAR:
                    return R.drawable.spr_cart_logo5;
                case GI_BAT:
                    return R.drawable.spr_cart_logo6;
                case GI_WHAT:
                    return R.drawable.spr_cart_logo7;
                case GI_OTHER:
                    return R.drawable.spr_cart_logo8;
                default:
                    throw new IllegalStateException("Unexpected value: " + iconShape);
            }
        }
    }


    public int getCartridgeShapeID(int cartridgeShape) {
        switch (cartridgeShape) {
            case GG_ROUND:
                return R.drawable.spr_cart1;
            case GG_NOTCH:
                return R.drawable.spr_cart2;
            case GG_FLAT:
                return R.drawable.spr_cart3;
            case GG_RIDGES:
                return R.drawable.spr_cart4;
            case GG_ZIGZAG:
                return R.drawable.spr_cart5;
            case GG_PRONGS:
                return R.drawable.spr_cart6;
            case GG_ANGLED:
                return R.drawable.spr_cart7;
            case GG_GBA:
                return R.drawable.spr_cart8;
            case GG_BIGNAME:
                return R.drawable.spr_cart9;
            default:
                throw new IllegalStateException("Unexpected value: " + cartridgeShape);
        }
    }


    public Bitmap getBitmapFromID(Context ctx, int ID) {
        BitmapDrawable drawableToDraw = (BitmapDrawable) AppCompatResources.getDrawable(ctx, ID);
        return drawableToDraw.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
    }

    public abstract class ShelfItem {
        public abstract Bitmap renderImage(Context ctx, int width, int height);
    }

    public class GameItem extends ShelfItem {
        int cartridgeColor;
        int cartridgeShape;
        int iconColor;
        int iconShape;

        public GameItem() {
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
        @Override
        public Bitmap renderImage(Context ctx, int width, int height) {
            Bitmap plane = Bitmap.createBitmap(28, 23, Bitmap.Config.ARGB_8888);
            Paint p = new Paint();
            Canvas PlaneCanvas = new Canvas(plane);
            int left = 2;
            int top = 2;
            plane.eraseColor(0x00FFFFFF);
            // b.eraseColor(0xFFFFFFFF);

            // SET ICON OFFSET
            switch (cartridgeShape) {
                case GG_NOTCH:
                    top = 4;
                    break;
                case GG_GBA:
                    left = 3;
                    break;
            }

            //Draw Cart
            Bitmap Cartridge = getBitmapFromID(ctx, getCartridgeShapeID(cartridgeShape));
            Canvas CartridgeCanvas = new Canvas(Cartridge);
            CartridgeCanvas.drawColor(getDIYColor(cartridgeColor), PorterDuff.Mode.MULTIPLY);
            PlaneCanvas.drawBitmap(Cartridge, 0, 0, p);

            //Draw Icon
            Bitmap Icon = getBitmapFromID(ctx, getCartridgeShapeID(cartridgeShape));
            Canvas IconCanvas = new Canvas(Icon);
            IconCanvas.drawColor(getDIYColor(iconColor), PorterDuff.Mode.LIGHTEN);
            PlaneCanvas.drawBitmap(Icon, left, top, p);

            Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            new Canvas(b).drawBitmap(plane, new Rect(0, 0, 26, 26), new Rect(0, 0, width, height), p);
            return b;

        }

    }

    public class RecordItem extends ShelfItem {

        int recordColor;
        int recordShape;
        int iconColor;
        int iconShape;

        public int getRecordColor() {
            return recordColor;
        }

        public void setRecordColor(int recordColor) {
            this.recordColor = recordColor;
        }

        public int getRecordShape() {
            return recordShape;
        }

        public void setRecordShape(int recordShape) {
            this.recordShape = recordShape;
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

        @Override
        @Deprecated
        public Bitmap renderImage(Context ctx, int width, int height) {
            // the final one
            Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            // the one to draw 16x16 icons directly
            Bitmap plane = Bitmap.createBitmap(26, 26, Bitmap.Config.ARGB_8888);
            // common paint
            Paint p = new Paint();
            // common canvas
            Canvas c = new Canvas(plane);
            int ResID;
            // don't touch these SWITCHES.
            // they will pollute your eyes
            // get cartridge drawable
            switch (recordShape) {
                case RR_CIRCLE:
                    ResID = R.drawable.spr_record1;
                    break;
                case RR_SQUARE:
                    ResID = R.drawable.spr_record2;
                    break;
                case RR_STAR:
                    ResID = R.drawable.spr_record3;
                    break;
                case RR_HEXAGON:
                    ResID = R.drawable.spr_record4;
                    break;
                case RR_CLOVER:
                    ResID = R.drawable.spr_record5;
                    break;
                case RR_DIAMOND:
                    ResID = R.drawable.spr_record6;
                    break;
                case RR_PLUS:
                    ResID = R.drawable.spr_record7;
                    break;
                case RR_FLOWER:
                    ResID = R.drawable.spr_record8;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + recordShape);
            }
            // fill with color, and draw them to the buffer
            plane.eraseColor(0x00FFFFFF);
            b.eraseColor(0x00FFFFFF);

            BitmapDrawable drawableToDraw = (BitmapDrawable) AppCompatResources.getDrawable(ctx, ResID);
            Bitmap temp = drawableToDraw.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            Canvas ccccccccccc = new Canvas(temp);
            ccccccccccc.drawColor(getDIYColor(recordColor), PorterDuff.Mode.MULTIPLY);
            c.drawBitmap(temp, 0, 0, p);
            // get icon drawable
            if (iconColor == recordColor) {
                switch (iconShape) {
                    case RI_HEART:
                        ResID = R.drawable.spr_record_logo1_r;
                        break;
                    case RI_FLOWER:
                        ResID = R.drawable.spr_record_logo2_r;
                        break;
                    case RI_STORM:
                        ResID = R.drawable.spr_record_logo3_r;
                        break;
                    case RI_CROSS:
                        ResID = R.drawable.spr_record_logo4_r;
                        break;
                    case RI_LEAF:
                        ResID = R.drawable.spr_record_logo5_r;
                        break;
                    case RI_DROPLET:
                        ResID = R.drawable.spr_record_logo6_r;
                        break;
                    case RI_LIGHTNING:
                        ResID = R.drawable.spr_record_logo7_r;
                        break;
                    case RI_SMILE:
                        ResID = R.drawable.spr_record_logo8_r;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + iconShape);
                }
            } else {
                switch (iconShape) {
                    case RI_HEART:
                        ResID = R.drawable.spr_record_logo1;
                        break;
                    case RI_FLOWER:
                        ResID = R.drawable.spr_record_logo2;
                        break;
                    case RI_STORM:
                        ResID = R.drawable.spr_record_logo3;
                        break;
                    case RI_CROSS:
                        ResID = R.drawable.spr_record_logo4;
                        break;
                    case RI_LEAF:
                        ResID = R.drawable.spr_record_logo5;
                        break;
                    case RI_DROPLET:
                        ResID = R.drawable.spr_record_logo6;
                        break;
                    case RI_LIGHTNING:
                        ResID = R.drawable.spr_record_logo7;
                        break;
                    case RI_SMILE:
                        ResID = R.drawable.spr_record_logo8;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + iconShape);
                }
            }
            drawableToDraw = (BitmapDrawable) AppCompatResources.getDrawable(ctx, ResID);
            temp = drawableToDraw.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            new Canvas(temp).drawColor(getDIYColor(iconColor), PorterDuff.Mode.MULTIPLY);
            c.drawBitmap(temp, 5, 5, p);
            // fill with color, and draw them to the buffer
            // then draw the buffer to a big picture
            new Canvas(b).drawBitmap(plane, new Rect(0, 0, 26, 26), new Rect(0, 0, width, height), p);
            return b;
        }
    }

    public class MangaItem extends ShelfItem {

        int mangaColor;
        int mangaShape;
        int iconColor;
        int iconShape;

        public MangaItem() {
        }

        public int getMangaColor() {
            return mangaColor;
        }

        public void setMangaColor(int mangaColor) {
            this.mangaColor = mangaColor;
        }

        public int getMangaShape() {
            return mangaShape;
        }

        public void setMangaShape(int mangaShape) {
            this.mangaShape = mangaShape;
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

        @Override
        @Deprecated
        public Bitmap renderImage(Context ctx, int width, int height) {
            // the final one
            Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            // the one to draw 16x16 icons directly
            Bitmap plane = Bitmap.createBitmap(26, 26, Bitmap.Config.ARGB_8888);
            // common paint
            Paint p = new Paint();
            // common canvas
            Canvas c = new Canvas(plane);
            int ResID;
            // don't touch these SWITCHES.
            // they will pollute your eyes
            // get cartridge drawable
            // fill with color, and draw them to the buffer
            plane.eraseColor(0x00FFFFFF);
            b.eraseColor(0x00FFFFFF);

            Bitmap temp = ((BitmapDrawable) AppCompatResources.getDrawable(ctx, R.drawable.spr_manga)).getBitmap().copy(Bitmap.Config.ARGB_8888, true);

            Canvas ccccccccccc = new Canvas(temp);
            ccccccccccc.drawColor(getDIYColor(mangaColor), PorterDuff.Mode.MULTIPLY);
            c.drawBitmap(temp, 0, 0, p);
            // get icon drawable
            if (iconColor == mangaColor) {
                switch (iconShape) {
                    case CI_LETTER:
                        ResID = R.drawable.spr_manga_logo1_r;
                        break;
                    case CI_HOUSE:
                        ResID = R.drawable.spr_manga_logo2_r;
                        break;
                    case CI_SHIELD:
                        ResID = R.drawable.spr_manga_logo3_r;
                        break;
                    case CI_SKULL:
                        ResID = R.drawable.spr_manga_logo4_r;
                        break;
                    case CI_CAT:
                        ResID = R.drawable.spr_manga_logo5_r;
                        break;
                    case CI_BAT:
                        ResID = R.drawable.spr_manga_logo6_r;
                        break;
                    case CI_SPACESHIP:
                        ResID = R.drawable.spr_manga_logo7_r;
                        break;
                    case CI_SMILE:
                        ResID = R.drawable.spr_manga_logo8_r;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + mangaShape);
                }
            } else {
                switch (mangaShape) {
                    case CI_LETTER:
                        ResID = R.drawable.spr_manga_logo1;
                        break;
                    case CI_HOUSE:
                        ResID = R.drawable.spr_manga_logo2;
                        break;
                    case CI_SHIELD:
                        ResID = R.drawable.spr_manga_logo3;
                        break;
                    case CI_SKULL:
                        ResID = R.drawable.spr_manga_logo4;
                        break;
                    case CI_CAT:
                        ResID = R.drawable.spr_manga_logo5;
                        break;
                    case CI_BAT:
                        ResID = R.drawable.spr_manga_logo6;
                        break;
                    case CI_SPACESHIP:
                        ResID = R.drawable.spr_manga_logo7;
                        break;
                    case CI_SMILE:
                        ResID = R.drawable.spr_manga_logo8;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + mangaShape);
                }
            }
            BitmapDrawable drawableToDraw = (BitmapDrawable) AppCompatResources.getDrawable(ctx, ResID);
            temp = drawableToDraw.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            new Canvas(temp).drawColor(getDIYColor(iconColor), PorterDuff.Mode.MULTIPLY);
            c.drawBitmap(temp, 5, 5, p);
            // fill with color, and draw them to the buffer
            // then draw the buffer to a big picture
            new Canvas(b).drawBitmap(plane, new Rect(0, 0, 26, 26), new Rect(0, 0, width, height), p);
            return b;
        }
    }

    // yes i copied them
    // especially manga_item, the top and left are COMPLETELY SAME AS RECORD_ITEM!!!!!!!!!!!
}
