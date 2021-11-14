package indi.nonoas.xbh.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * @author Nonoas
 * @date 2021/11/14
 */
public class SpaceItem extends DrawerItem<SpaceItem.ViewHolder> {

    private final int spaceDp;

    public SpaceItem(int spaceDp) {
        this.spaceDp = spaceDp;
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        Context c = parent.getContext();
        View view = new View(c);
        int height = c.getResources().getDisplayMetrics().densityDpi * spaceDp;
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height
        ));
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder) {

    }

    static class ViewHolder extends DrawerAdapter.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public boolean isSelected() {
        return false;
    }
}
