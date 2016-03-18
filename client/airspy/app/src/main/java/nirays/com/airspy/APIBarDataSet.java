package nirays.com.airspy;

import android.graphics.Color;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

/**
 * Created by kirant400 on 18/3/16.
 */
public class APIBarDataSet extends BarDataSet {


    public APIBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
        String[] valRanges =  {
                "50",
        "100",
        "200",
        "300",
        "400",
        "500"
        };
        String[] valRangeColor = {
                "#1BCA21",
                "#89df46",
                "#E8E721",
                "#E86F21",
                "#f24242",
                "#db0000"
        };
        int retColor = Color.parseColor("#db0000");
        for (int i = 0;i<valRanges.length;i++
                ) {
            int valRange = Integer.parseInt(valRanges[i]);
            if (index<= valRange){
                retColor = Color.parseColor(valRangeColor[i]);
                break;
            }
        }
        return retColor;
    }

}
