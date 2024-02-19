package com.x8192Bit.DIYEdit_Mobile.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.InputStream;

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
        InputStream is = ctx.getResources().openRawResource(ID);
        return BitmapFactory.decodeStream(is).copy(Bitmap.Config.ARGB_8888, true);
    }

    public int getRecordShapeID(int recordShape) {
        switch (recordShape) {
            case RR_CIRCLE:
                return R.drawable.spr_record1;
            case RR_SQUARE:
                return R.drawable.spr_record2;
            case RR_STAR:
                return R.drawable.spr_record3;
            case RR_HEXAGON:
                return R.drawable.spr_record4;
            case RR_CLOVER:
                return R.drawable.spr_record5;
            case RR_DIAMOND:
                return R.drawable.spr_record6;
            case RR_PLUS:
                return R.drawable.spr_record7;
            case RR_FLOWER:
                return R.drawable.spr_record8;
            default:
                throw new IllegalStateException("Unexpected value: " + recordShape);
        }
    }

    public int getRecordIconID(boolean isReversed, int iconShape) {
        if (isReversed) {
            switch (iconShape) {
                case RI_HEART:
                    return R.drawable.spr_record_logo1_r;
                case RI_FLOWER:
                    return R.drawable.spr_record_logo2_r;
                case RI_STORM:
                    return R.drawable.spr_record_logo3_r;
                case RI_CROSS:
                    return R.drawable.spr_record_logo4_r;
                case RI_LEAF:
                    return R.drawable.spr_record_logo5_r;
                case RI_DROPLET:
                    return R.drawable.spr_record_logo6_r;
                case RI_LIGHTNING:
                    return R.drawable.spr_record_logo7_r;
                case RI_SMILE:
                    return R.drawable.spr_record_logo8_r;
                default:
                    throw new IllegalStateException("Unexpected value: " + iconShape);
            }
        } else {
            switch (iconShape) {
                case RI_HEART:
                    return R.drawable.spr_record_logo1;
                case RI_FLOWER:
                    return R.drawable.spr_record_logo2;
                case RI_STORM:
                    return R.drawable.spr_record_logo3;
                case RI_CROSS:
                    return R.drawable.spr_record_logo4;
                case RI_LEAF:
                    return R.drawable.spr_record_logo5;
                case RI_DROPLET:
                    return R.drawable.spr_record_logo6;
                case RI_LIGHTNING:
                    return R.drawable.spr_record_logo7;
                case RI_SMILE:
                    return R.drawable.spr_record_logo8;
                default:
                    throw new IllegalStateException("Unexpected value: " + iconShape);
            }
        }
    }

    int getMangaIconID(boolean isReversed, int iconShape) {
        if (isReversed) {
            switch (iconShape) {
                case CI_LETTER:
                    return R.drawable.spr_manga_logo1_r;
                case CI_HOUSE:
                    return R.drawable.spr_manga_logo2_r;
                case CI_SHIELD:
                    return R.drawable.spr_manga_logo3_r;
                case CI_SKULL:
                    return R.drawable.spr_manga_logo4_r;
                case CI_CAT:
                    return R.drawable.spr_manga_logo5_r;
                case CI_BAT:
                    return R.drawable.spr_manga_logo6_r;
                case CI_SPACESHIP:
                    return R.drawable.spr_manga_logo7_r;
                case CI_SMILE:
                    return R.drawable.spr_manga_logo8_r;
                default:
                    throw new IllegalStateException("Unexpected value: " + iconShape);
            }
        } else {
            switch (iconShape) {
                case CI_LETTER:
                    return R.drawable.spr_manga_logo1;
                case CI_HOUSE:
                    return R.drawable.spr_manga_logo2;
                case CI_SHIELD:
                    return R.drawable.spr_manga_logo3;
                case CI_SKULL:
                    return R.drawable.spr_manga_logo4;
                case CI_CAT:
                    return R.drawable.spr_manga_logo5;
                case CI_BAT:
                    return R.drawable.spr_manga_logo6;
                case CI_SPACESHIP:
                    return R.drawable.spr_manga_logo7;
                case CI_SMILE:
                    return R.drawable.spr_manga_logo8;
                default:
                    throw new IllegalStateException("Unexpected value: " + iconShape);
            }
        }
    }

    public abstract class ShelfItem {
        public abstract Drawable renderImage(Context ctx);
    }

    public class GameItem extends ShelfItem {
        int cartridgeColor;
        int cartridgeShape;
        int iconColor;
        int iconShape;


        //region getters and setters
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

        //endregion
        @Override
        public Drawable renderImage(Context ctx) {
            Bitmap plane = Bitmap.createBitmap(28, 23, Bitmap.Config.ARGB_8888);
            Paint p = new Paint();
            Canvas PlaneCanvas = new Canvas(plane);
            int left = 2;
            int top = 2;
            plane.eraseColor(0x00FFFFFF);
            boolean isIconColorRender = true;

            // SET ICON OFFSET
            switch (cartridgeShape) {
                case GG_NOTCH:
                    top = 4;
                    break;
                case GG_GBA:
                    left = 3;
                    break;
                case GG_BIGNAME:
                    isIconColorRender = false;
                    break;
            }

            //Draw Cart
            Bitmap Cartridge = getBitmapFromID(ctx, getCartridgeShapeID(cartridgeShape));
            Canvas CartridgeCanvas = new Canvas(Cartridge);
            if (isIconColorRender) {
                if (cartridgeColor == BLACK) {
                    CartridgeCanvas.drawColor(getDIYColor(cartridgeColor), PorterDuff.Mode.MULTIPLY);
                } else {
                    CartridgeCanvas.drawColor(getDIYColor(cartridgeColor), PorterDuff.Mode.MULTIPLY);
                }
            }
            PlaneCanvas.drawBitmap(Cartridge, 0, 0, p);

            if (isIconColorRender) {
                //Draw Icon
                Bitmap Icon = getBitmapFromID(ctx, getCartridgeIconID(cartridgeColor == iconColor, iconShape));
                Canvas IconCanvas = new Canvas(Icon);
                if (iconColor == BLACK) {
                    IconCanvas.drawColor(getDIYColor(iconColor), PorterDuff.Mode.LIGHTEN);
                } else {
                    IconCanvas.drawColor(getDIYColor(iconColor), PorterDuff.Mode.MULTIPLY);
                }
                PlaneCanvas.drawBitmap(Icon, left, top, p);
            }

            BitmapDrawable d = new BitmapDrawable(plane);
            d.setAntiAlias(false);

            return d;
        }

    }

    public class RecordItem extends ShelfItem {

        int recordColor;
        int recordShape;
        int iconColor;
        int iconShape;

        //region getters and setters

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

        //endregion

        @Override
        public Drawable renderImage(Context ctx) {
            Bitmap plane = Bitmap.createBitmap(26, 26, Bitmap.Config.ARGB_8888);
            Paint p = new Paint();
            Canvas PlaneCanvas = new Canvas(plane);
            plane.eraseColor(0x00FFFFFF);

            //Draw Record
            Bitmap Record = getBitmapFromID(ctx, getRecordShapeID(recordShape));
            Canvas RecordCanvas = new Canvas(Record);
            RecordCanvas.drawColor(getDIYColor(recordColor), PorterDuff.Mode.MULTIPLY);
            PlaneCanvas.drawBitmap(Record, 0, 0, p);

            //Draw Icon
            Bitmap Icon = getBitmapFromID(ctx, getRecordIconID(recordColor == iconColor, iconShape));
            Canvas IconCanvas = new Canvas(Icon);
            IconCanvas.drawColor(getDIYColor(iconColor), PorterDuff.Mode.MULTIPLY);
            PlaneCanvas.drawBitmap(Icon, 5, 5, p);

            BitmapDrawable d = new BitmapDrawable(plane);
            d.setAntiAlias(false);
            return d;
        }
    }

    public class MangaItem extends ShelfItem {

        int mangaColor;
        int iconColor;
        int iconShape;

        //region getters and setters
        public int getMangaColor() {
            return mangaColor;
        }

        public void setMangaColor(int mangaColor) {
            this.mangaColor = mangaColor;
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
        //endregion

        @Override
        public Drawable renderImage(Context ctx) {
            Bitmap plane = Bitmap.createBitmap(26, 26, Bitmap.Config.ARGB_8888);
            Paint p = new Paint();
            Canvas PlaneCanvas = new Canvas(plane);
            plane.eraseColor(0x00FFFFFF);

            //Draw Manga
            Bitmap Manga = getBitmapFromID(ctx, R.drawable.spr_manga);
            Canvas MangaCanvas = new Canvas(Manga);
            MangaCanvas.drawColor(getDIYColor(mangaColor), PorterDuff.Mode.MULTIPLY);
            PlaneCanvas.drawBitmap(Manga, 0, 0, p);

            //Draw Icon
            Bitmap Icon = getBitmapFromID(ctx, getMangaIconID(mangaColor == iconColor, iconShape));
            Canvas IconCanvas = new Canvas(Icon);
            IconCanvas.drawColor(getDIYColor(iconColor), PorterDuff.Mode.MULTIPLY);
            PlaneCanvas.drawBitmap(Icon, 5, 5, p);

            BitmapDrawable d = new BitmapDrawable(plane);
            d.setAntiAlias(false);

            return d;
        }
    }

    // yes i copied them
    // especially manga_item, the top and left are COMPLETELY SAME AS RECORD_ITEM!!!!!!!!!!!
}
