package com.uni.julio.supertvplus.binding;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;
import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.R;

public class BindingAdapters {

    @BindingAdapter({"hidden"})
    public static void bindHiddenVisibility(View view, boolean hidden) {
        view.setVisibility(hidden ? View.GONE : View.VISIBLE);
    }
    @BindingAdapter({"visibleText"})
    public static void visibleText(View view, String text) {
        if(text == null ) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(text.equals("") ? View.GONE : View.VISIBLE);
    }
    @BindingAdapter({"visibleText"})
    public static void visibleInt(View view, int text) {

        view.setVisibility(text == 0 ? View.GONE : View.VISIBLE);
    }
    @BindingAdapter({"invisible"})
    public static void bindInvisibleVisibility(View view, boolean invisible) {
        view.setVisibility(invisible ? View.GONE : View.VISIBLE);
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
    @BindingAdapter({"setTitleEpisode"})
    public static void setTitleEpisode(TextView view, int text){
        view.setText("Episode "+(text+1));
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
        }
        else {
//            retrieveImage(url, imageView);
//            Picasso.with(imageView.getContext()).load(R.drawable.imageview_placeholder).placeholder(R.drawable.imageview_placeholder).into(imageView);
            if(imageView.getId() == R.id.mark_img || imageView.getId() == R.id.channel_icon){
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
    @BindingAdapter({"showLike"})
    public static void bindLikeButton(LinearLayout linearLayout,boolean liked){
        linearLayout.setBackground(LiveTvApplication.getAppContext().getResources().getDrawable(liked ? R.drawable.button_like_active : R.drawable.button_like_normal));
    }

    @BindingAdapter({"showDislike"})
    public static void bindDislikeButton(LinearLayout linearLayout,boolean disliked){
        linearLayout.setBackground(LiveTvApplication.getAppContext().getResources().getDrawable(disliked ? R.drawable.button_dislike_active : R.drawable.button_dislike_normal));
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
