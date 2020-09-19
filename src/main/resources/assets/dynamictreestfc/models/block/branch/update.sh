for i in ../../../trees/*.txt; do tree=`echo $i|sed -e s/.txt// -e s,^.*/trees/,,`; echo $tree; sed -e s/white_cedar/$tree/ base.json >$tree.json ; done
