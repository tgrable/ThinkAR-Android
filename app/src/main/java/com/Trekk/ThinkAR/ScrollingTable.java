package com.Trekk.ThinkAR;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by timgrable1 on 9/26/16.
 */

public class ScrollingTable extends LinearLayout {
    public ScrollingTable( Context context )
    {
        super( context );
    }

    public ScrollingTable( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout( changed, l, t, r, b );
        List<Integer> colWidths = new LinkedList<Integer>();

        TableLayout header = (TableLayout) findViewById( R.id.vendor_header );
        TableLayout body = (TableLayout) findViewById( R.id.vendor_table );

        for ( int rownum = 0; rownum < body.getChildCount(); rownum++ )
        {
            TableRow row = (TableRow) body.getChildAt( rownum );
            for ( int cellnum = 0; cellnum < row.getChildCount(); cellnum++ )
            {
                View cell = row.getChildAt( cellnum );
                Integer cellWidth = cell.getWidth();

                if ( colWidths.size() <= cellnum )
                {
                    colWidths.add( cellWidth );
                }
                else
                {
                    Integer current = colWidths.get( cellnum );
                    if( cellWidth > current )
                    {
                        colWidths.remove( cellnum );
                        colWidths.add( cellnum, cellWidth );
                    }
                }
            }
        }

        for ( int rownum = 0; rownum < header.getChildCount(); rownum++ )
        {
            TableRow row = (TableRow) header.getChildAt( rownum );
            for ( int cellnum = 0; cellnum < row.getChildCount(); cellnum++ )
            {
                View cell = row.getChildAt( cellnum );
                TableRow.LayoutParams params = (TableRow.LayoutParams)cell.getLayoutParams();
                params.width = colWidths.get( cellnum );
            }
        }
    }
}
