package br.com.dbm.core.sql.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Leandro Tonietto
 */
public class PrimaryKeyColumns {

   private ArrayList<TableAttribute> columns;
   private String primaryKeyName;

   public PrimaryKeyColumns() {
      columns = new ArrayList();
   }

   public PrimaryKeyColumns(String pkName) {
      this();
      this.primaryKeyName = pkName;
   }

   public ArrayList<TableAttribute> getColumns() {
      return columns;
   }

   public void setColumns(ArrayList<TableAttribute> columns) {
      this.columns = columns;
   }

   public void addColumn(TableAttribute col) {
      columns.add(col);
   }

   public String getPrimaryKeyName() {
      return primaryKeyName;
   }

   public void setPrimaryKeyName(String primaryKeyName) {
      this.primaryKeyName = primaryKeyName;
   }

}
