package com.uni.julio.supertvplus.adapter;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.helper.TVRecyclerViewAdapter;
import com.uni.julio.supertvplus.listeners.LiveProgramSelectedListener;
import com.uni.julio.supertvplus.service.test.GetSpeedTestHostsHandler;
import com.uni.julio.supertvplus.utils.Device;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerAdapter extends TVRecyclerViewAdapter<ServerAdapter.MyViewHolder> {

    private Context mContext;
    private HashMap<Integer, List<String>> tempServers = new HashMap<>();
    private HashMap<Integer, List<String>> servers = new HashMap<>();

    private TVRecyclerView recyclerView;
    private List<Integer> keys = new ArrayList<>();
    private LiveProgramSelectedListener liveProgramSelectedListener;

    public ServerAdapter(Context context , HashMap<Integer, List<String>> servers, TVRecyclerView recyclerView, LiveProgramSelectedListener liveProgramSelectedListener) {
        mContext=context;
        this.servers = servers;
        setSearchResult("");
        this.recyclerView=recyclerView;
        this.liveProgramSelectedListener = liveProgramSelectedListener;
        if(!Device.canTreatAsBox()) recyclerView.setIsAutoProcessFocus(false);
        this.recyclerView.setOnItemStateListener(new TVRecyclerView.OnItemStateListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                ServerAdapter.this.liveProgramSelectedListener.onLiveProgramSelected(null, (Integer) view.getTag());
            }

            @Override
            public void onItemViewFocusChanged(boolean gainFocus, View view, int position) {
               /* if(view == null) return;
                if(gainFocus) {
                    view.findViewById(R.id.fl_main_layout).setBackground(mContext.getResources().getDrawable(R.drawable.moviesborder));
                }else{
                    view.findViewById(R.id.fl_main_layout).setBackground(mContext.getResources().getDrawable(R.drawable.md_transparent));
                }*/
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.item_server, viewGroup, false);
        return new MyViewHolder(mContext,convertView);
    }

    public void setSearchResult(String location){
        try{
            tempServers.clear();
            keys.clear();
            for(int i = 0; i<servers.size(); i++){
                if(location.equals("") || servers.get(i).get(5).contains(location) || servers.get(i).get(3).contains(location)){
                    tempServers.put(i, servers.get(i));
                    keys.add(i);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mContext, "Unknown Error", Toast.LENGTH_LONG).show();
            tempServers.clear();
            keys.clear();
        }
        this.notifyDataSetChanged();
    }
    @Override
    protected void focusOut(View v, int position) {

    }
    @Override
    protected void focusIn(View v, int position) {

    }

    @Override
    protected void onDataBinding(MyViewHolder holder, int position) {
        double selfLat = GetSpeedTestHostsHandler.selfLat;
        double selfLon = GetSpeedTestHostsHandler.selfLon;
        Location source = new Location("Source");
        source.setLatitude(selfLat);
        source.setLongitude(selfLon);
        List<String> ls = tempServers.get(keys.get(position));
        Location dest = new Location("Dest");
        dest.setLatitude(Double.parseDouble(ls.get(0)));
        dest.setLongitude(Double.parseDouble(ls.get(1)));
        final double distance = source.distanceTo(dest);
        holder.getViewDataBinding().getRoot().setTag(keys.get(position));
        holder.getViewDataBinding().setVariable(com.uni.julio.supertvplus.BR.serverAdapter, this);
        ((TextView)holder.getViewDataBinding().getRoot().findViewById(R.id.serverName)).setText(tempServers.get(keys.get(position)).get(5));
        ((TextView)holder.getViewDataBinding().getRoot().findViewById(R.id.serverLocation)).setText(tempServers.get(keys.get(position)).get(3));;
        ((TextView)holder.getViewDataBinding().getRoot().findViewById(R.id.distance)).setText(String.format(new DecimalFormat("#.##").format(distance / 1000))+"km");
        holder.getViewDataBinding().executePendingBindings();
    }
    public void onClickItem(View view) {
        liveProgramSelectedListener.onLiveProgramSelected(null, (Integer) view.getTag());
    }
    @Override
    public int getItemCount() {
        return tempServers.size();
    }
    class MyViewHolder extends TVRecyclerViewAdapter.ViewHolder{
        private ViewDataBinding viewDataBinding;
        MyViewHolder(Context context, View itemView){
            super(context,itemView);
            viewDataBinding= DataBindingUtil.bind(itemView);

        }
        ViewDataBinding getViewDataBinding(){
            return viewDataBinding;
        }
    }

}
