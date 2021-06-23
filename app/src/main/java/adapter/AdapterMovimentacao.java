        package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.miguelxcruz.finatialorganizer.R;

import java.util.List;

import model.Movimentation;


public class AdapterMovimentacao extends RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder> {

    List<Movimentation> movimentations;
    Context context;

    public AdapterMovimentacao(List<Movimentation> movimentations, Context context) {
        this.movimentations = movimentations;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movimentacao, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movimentation movimentation = movimentations.get(position);

        holder.title.setText(movimentation.getDescription());
        holder.value.setText(String.valueOf(movimentation.getValue()));
        holder.category.setText(movimentation.getCategory());


        if (movimentation.getType().equals("e")){
            holder.value.setTextColor(context.getResources().getColor(R.color.earning));
        }

       if (movimentation.getType().equals("s")){
            holder.value.setTextColor(context.getResources().getColor(R.color.spending));
            holder.value.setText("-" + movimentation.getValue());
        }
    }


    @Override
    public int getItemCount() {
        return movimentations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, value, category;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textAdapterTitulo);
            value = itemView.findViewById(R.id.textAdapterValor);
            category = itemView.findViewById(R.id.textAdapterCategoria);
        }

    }

}
