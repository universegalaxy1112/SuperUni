package com.uni.julio.supertv.binding;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;
import com.uni.julio.supertv.R;

public class BindingAdapters {

    @BindingAdapter({"hidden"})
    public static void bindHiddenVisibility(View view, boolean hidden) {
        view.setVisibility(hidden ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter({"invisible"})
    public static void bindInvisibleVisibility(View view, boolean invisible) {
        view.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
    }
    @BindingAdapter({"setDescription"})
    public static void setDescription(TextView view, String text) {
        if(text==null||text.equals("")){
            view.setVisibility(View.GONE);
            ((ViewGroup)view.getParent()).findViewById(R.id.description).setVisibility(View.GONE);
        }else{
            view.setVisibility(View.VISIBLE);
            ((ViewGroup)view.getParent()).findViewById(R.id.description).setVisibility(View.VISIBLE);

            view.setText(text);
        }
    }
    @BindingAdapter({"setActors"})
    public static void setActors(TextView view, String text) {

        if(text==null||text.equals("")){
            view.setVisibility(View.GONE);
            ((ViewGroup)view.getParent()).findViewById(R.id.actors).setVisibility(View.GONE);
        }
        else{
            view.setVisibility(View.VISIBLE);
            view.setText(text);
            ((ViewGroup)view.getParent()).findViewById(R.id.actors).setVisibility(View.VISIBLE);
        }
    }
    @BindingAdapter({"setDirector"})
    public static void setDirector(TextView view, String text) {
        if(text==null||text.equals("")){
            view.setVisibility(View.GONE);
            ((ViewGroup)view.getParent()).findViewById(R.id.director).setVisibility(View.GONE);
        }
        else{
            view.setVisibility(View.VISIBLE);
            view.setText(text);
            ((ViewGroup)view.getParent()).findViewById(R.id.director).setVisibility(View.VISIBLE);

        }
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(final ImageView imageView, String url) {
        if(TextUtils.isEmpty(url)) {
            return;
        }
        if(url.equals("lupita")) {
            imageView.setImageResource(R.drawable.ic_search_black_24dp);
        }
        else {
//            retrieveImage(url, imageView);
//            Picasso.with(imageView.getContext()).load(R.drawable.imageview_placeholder).placeholder(R.drawable.imageview_placeholder).into(imageView);
            if(imageView.getId()==R.id.mark_img){
                Picasso.get().load(url).placeholder(R.drawable.channel).into(imageView);
            }
            else{
                Picasso.get().load(url).placeholder(R.drawable.placeholder).into(imageView);

            }
        }
    }


    @BindingAdapter("imageId")
    public static void setImage(ImageView imageView, int imageId) {
        imageView.setImageResource(imageId);
    }

    @BindingAdapter("showDuration")
    public static void setDuration(TextView textView, int seconds) {
        if(seconds <= 0) {
            textView.setVisibility(View.GONE);
            return;
        }
        try {
            String duration = "";
            int hours = seconds / 3600;
            int minutes = (seconds% 3600) / 60;

            duration = String.format("%01dh %02dmin", hours, minutes);
            textView.setText(duration);
        }catch (Exception e) { textView.setText(""); }
    }

    @BindingAdapter("showRating")
    public static void setRating(RatingBar ratingBar, int rating) {
        float newRating = rating / 20;
         try {
            ratingBar.setRating(newRating);
        }catch (Exception e) { ratingBar.setRating(0f); }
    }

    @BindingAdapter({"showHDIcon"})
    public static void bindShowHDIcon(TextView view, boolean isHD) {
        view.setVisibility(isHD? View.VISIBLE:View.GONE);

    }

    @BindingAdapter({"showSeenIcon"})
    public static void bindShowSeenIcon(ImageView view, boolean seen) {
/*
        view.setImageResource(seen ? R.drawable.seen_icon : R.drawable.seen_icon_disabled);
*/
    }

    @BindingAdapter({"showFavoriteIcon"})
    public static void bindShowFavoriteIcon(ImageView view, boolean favorite) {
        view.setImageResource(favorite ? R.drawable.ic_favorite_like : R.drawable.ic_favorite_normal);

    }

    @BindingAdapter({"setDate"})
    public static void setDate(TextView view, String date) {
        view.setText(date.substring(0,date.indexOf(" ")));
    }

    @BindingAdapter({"justifyText"})
    public static void justifyTextView(TextView view, String text) {
        text = "<html><body style=\"text-align:justify\">" + text + "</body></Html>";
        view.setText(fromHtml(text));
    }

    @BindingAdapter({"loadData"})
    public static void loadDataToWebView(WebView view, String text) {
        text = "<html><body style=\"text-align:justify\">" + text + "</body></Html>";
        view.loadData(String.format(" %s ", text), "text/html", "utf-8");
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}