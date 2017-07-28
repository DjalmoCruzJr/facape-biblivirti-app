package org.sysmob.biblivirti.comparators;

import org.sysmob.biblivirti.model.Material;

import java.util.Comparator;

/**
 * Created by micro12 on 28/07/2017.
 */

public class MaterialComparatorByMacaldt implements Comparator<Material> {

    @Override
    public int compare(Material m1, Material m2) {
        return m2.getMadcadt().compareTo(m1.getMadcadt());
    }
}
