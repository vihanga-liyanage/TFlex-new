package classes;

import static classes.DBConnection.logger;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Vihanga Liyanage
 */
public class PDF {

    private final String font = "Segoe UI Light";
    private final String path = "C:\\TFlex\\";
    private final String imgPath = "src\\img\\";

    public PDF() {
    }

    //private methods ==========================================================
    private PdfPCell getTableHeaderCell(String name) {
        PdfPCell cell = new PdfPCell(new Paragraph(name, FontFactory.getFont(font, 10, Font.BOLD, BaseColor.WHITE)));
        cell.setPadding(5);
        cell.setPaddingBottom(7);
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        return cell;
    }

    private PdfPCell getIngCategoryCell(String name) {
        PdfPCell cell = new PdfPCell(new Paragraph(name, FontFactory.getFont(font, 9, Font.BOLD, BaseColor.WHITE)));
        cell.setPadding(2);
        cell.setPaddingBottom(3);
        cell.setBackgroundColor(BaseColor.GRAY);
        cell.setColspan(100);
        return cell;
    }

    private PdfPCell getTableDataCell(String name) {
        PdfPCell cell = new PdfPCell(new Paragraph(name, FontFactory.getFont(font, 9, Font.NORMAL, BaseColor.BLACK)));
        cell.setPadding(3);
        cell.setPaddingBottom(4);
        cell.setBackgroundColor(new BaseColor(242, 242, 242));
        return cell;
    }

    private PdfPCell getHeaderNameCell(String name) {
        PdfPCell cell = new PdfPCell(new Paragraph(name, FontFactory.getFont(font, 9, Font.BOLD, BaseColor.BLACK)));
        cell.setPadding(2);
        cell.setPaddingBottom(3);
        cell.setBackgroundColor(new BaseColor(242, 242, 242));
        return cell;
    }

    private PdfPCell getHeaderDataCell(String name) {
        PdfPCell cell = new PdfPCell(new Paragraph(name, FontFactory.getFont(font, 9, Font.BOLD, BaseColor.BLACK)));
        cell.setPadding(2);
        cell.setPaddingBottom(3);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    //private method for po pdfs
    private PdfPCell POLogo(String path) {
        Image img = null;
        try {
            img = Image.getInstance(path);
        } catch (BadElementException | IOException ex) {
            logger.log(Level.WARNING, "BadElementException | IOException : " + ex.getMessage());
        }
        img.scaleToFit(300f, 150f);
        PdfPCell cell = new PdfPCell();
        cell.addElement(img);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(BaseColor.GRAY);
        cell.setBorderWidthBottom(1);
        return cell;
    }

    private PdfPCell POHeader(String text) {
        String font = "Segoe UI Semilight";
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, FontFactory.getFont(font, 20, Font.BOLD));
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(BaseColor.GRAY);
        cell.setBorderWidthBottom(1);
        return cell;
    }

    private PdfPCell SupName(String text) {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, FontFactory.getFont(font, 13, Font.BOLD));
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(BaseColor.GRAY);
        cell.setBorderWidthBottom(1);
        cell.setPadding(10);

        return cell;
    }

    private PdfPCell PODetails(String text) {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, FontFactory.getFont(font, 10, BaseColor.BLACK));
        p.setAlignment(Element.ALIGN_JUSTIFIED);

        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(BaseColor.GRAY);
        cell.setBorderWidthBottom(1);
        cell.setPadding(10);
        //cell.setBorder(Rectangle.);
        return cell;
    }

    private PdfPCell tableHeaderCellPO(String name) {
        PdfPCell cell = new PdfPCell(new Paragraph(name, FontFactory.getFont(font, 10, Font.BOLD, BaseColor.BLACK)));
        cell.setPadding(5);
        cell.setPaddingBottom(7);

        return cell;
    }

    private PdfPCell tableCellPO(String name) {
        PdfPCell cell = new PdfPCell(new Paragraph(name, FontFactory.getFont(font, 10, BaseColor.BLACK)));
        cell.setPadding(5);
        cell.setPaddingBottom(7);

        return cell;
    }

    private PdfPCell discountAndTaxexTopic(String name) {
        String font = "Segoe UI Semilight";
        Paragraph p = new Paragraph(name, FontFactory.getFont(font, 10, Font.BOLD, BaseColor.BLACK));
        PdfPCell cell = new PdfPCell(p);
        cell.setPaddingTop(7);
        cell.setPaddingBottom(5);
        return cell;
    }

    private PdfPCell discountAndTaxexData(String text) {
        String font = "Segoe UI Semilight";
        Paragraph p = new Paragraph(text, FontFactory.getFont(font, 10, BaseColor.BLACK));
        p.setAlignment(Element.ALIGN_LEFT);
        PdfPCell cell = new PdfPCell(p);
        cell.setPaddingTop(7);
        cell.setPaddingBottom(5);
        return cell;
    }

    public static PdfPCell companyNameAndDate(String name) {
        String font = "HELVETICA_OBLIQUE";
        Paragraph p = new Paragraph(name, FontFactory.getFont(font, 10, Font.ITALIC, BaseColor.BLACK));
        p.setAlignment(Element.ALIGN_LEFT);
        PdfPCell cell = new PdfPCell(p);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingTop(7);
        //cell.setPaddingBottom(5);
        return cell;
    }

    //end of private methods ===================================================
    //start of methods for calculations==================================================
    private float parseFloat(String num) {
        try {
            return Float.parseFloat(num);
        } catch (NumberFormatException e) {
            if (num.matches("[[0-9]{1,2}+,]*.[0-9]*")) {
                num = num.replace(",", "");
                return Float.parseFloat(num);
            }
        }
        return 0;
    }

    private String formatNum(String num) {
        String decimal = num, point = null;
        if (num.contains(".")) {
            String[] temp = num.split("\\.");
            decimal = temp[0];
            point = temp[1];
        }
        int i = decimal.length();
        while (i > 3) {
            String part1 = decimal.substring(0, i - 3);
            String part2 = decimal.substring(i - 3);
            decimal = part1 + "," + part2;
            i -= 3;
        }
        if (point != null) {
            decimal += "." + point;
        }
        return decimal;
    }

    public static float round(float num, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    private String formatNum(float num) {
        num = round(num, 2);
        return formatNum(Float.toString(num));
    }

    //end of methods for calculations==================================================
    public void generateMasterPlanPDF(JTable table, String[] data) {
        try {
            Document doc = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);

            //Creating the directory for the order
            String tempPath = path + "RM-Orders\\" + data[0] + "\\";
            new File(tempPath).mkdirs();

            PdfWriter.getInstance(doc, new FileOutputStream(tempPath + "RM_Order_Master_Plan-" + data[0] + ".pdf"));
            doc.open();

            float[] coloumWidths = {5, 2, 2, 2, 2, 2, 2, 5};
            PdfPTable masterTable = new PdfPTable(coloumWidths);
            masterTable.setWidthPercentage(100);

            //Adding logo
            PdfPCell logoCell = new PdfPCell(Image.getInstance(imgPath + "logo.png"));

            logoCell.setColspan(3);
            masterTable.addCell(logoCell);

            //Adding master plan header data as another table
            coloumWidths = new float[]{5.2f, 5};
            PdfPTable headerTable = new PdfPTable(coloumWidths);
            PdfPCell titleCell = new PdfPCell(new Paragraph("RAW MATERIAL ORDERING MASTER PLAN", FontFactory.getFont(font, 11, Font.BOLD)));
            titleCell.setPadding(15);
            titleCell.setColspan(10);
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerTable.addCell(titleCell);
            headerTable.addCell(getHeaderNameCell("MASTER PLAN #"));
            headerTable.addCell(getHeaderDataCell(data[0]));
            headerTable.addCell(getHeaderNameCell("DATE"));
            headerTable.addCell(getHeaderDataCell(data[1]));
            headerTable.addCell(getHeaderNameCell("Client"));
            headerTable.addCell(getHeaderDataCell(""));
            headerTable.addCell(getHeaderNameCell("Invoice Ref"));
            headerTable.addCell(getHeaderDataCell("SAMPLES"));

            PdfPCell headerDataCell = new PdfPCell(headerTable);
            headerDataCell.setColspan(5);
            masterTable.addCell(headerDataCell);

            //Adding master table headers
            masterTable.addCell(getTableHeaderCell("Item Description"));
            masterTable.addCell(getTableHeaderCell("Qty Needed"));
            masterTable.addCell(getTableHeaderCell("Visible Stock"));
            masterTable.addCell(getTableHeaderCell("Invisible Stock"));
            masterTable.addCell(getTableHeaderCell("Balance"));
            masterTable.addCell(getTableHeaderCell("Excess Qty"));
            masterTable.addCell(getTableHeaderCell("Final Order"));
            masterTable.addCell(getTableHeaderCell("Supplier Name"));

            //Adding data from master table
            String category = table.getValueAt(0, 8).toString();
            masterTable.addCell(getIngCategoryCell(category.toUpperCase()));

            for (int i = 0; i < table.getRowCount(); i++) {
                if (!category.equals(table.getValueAt(i, 8).toString())) {
                    category = table.getValueAt(i, 8).toString();
                    masterTable.addCell(getIngCategoryCell(category.toUpperCase()));
                }
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 0).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 1).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 2).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 3).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 4).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 5).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 6).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 7).toString()));
            }
            doc.add(masterTable);

            //powered by
            PdfPTable poweredBy = new PdfPTable(1);
            poweredBy.setWidthPercentage(100);
            poweredBy.setSpacingBefore(5);
            poweredBy.addCell(companyNameAndDate("Powered By : Reid Solutions "));
            doc.add(poweredBy);

            //copyright
            PdfPTable copyRight = new PdfPTable(1);
            copyRight.setWidthPercentage(100);
            copyRight.setSpacingBefore(5);
            copyRight.addCell(companyNameAndDate("\u00a9" + "  2016 Reid Solutions All RIGHTS RESERVED"));
            doc.add(copyRight);
                
            doc.close();

        } catch (FileNotFoundException | DocumentException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : PDF 314", "Error", 0);
            System.exit(0);
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : PDF 317", "Error", 0);
            System.exit(0);
        }
    }

    public String generateSupplierwisePO(Set<String> supplierList, List<List> mainList, List<List> mainList2, String orderID, List discountList, List taxList, List totalList) {
        int pdfOK = 1;
        int count = 0;
        String[] supNameArray = supplierList.toArray(new String[supplierList.size()]);
        String tempPath = path + "RM-Orders\\" + orderID + "\\";
        for (List lst : mainList) {
            Document doc = new Document();
            String suppName = "";
            try {

                suppName = supNameArray[count];
                String fileName = suppName.concat(".pdf");
                PdfWriter.getInstance(doc, new FileOutputStream(tempPath + fileName));
            } catch (DocumentException ex) {
                //Logger.getLogger(OrderConfirmation.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("DocumentException : " + ex);
                pdfOK = 0;
            } catch (FileNotFoundException ex) {
                //Logger.getLogger(OrderConfirmation.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("FileNotFoundException : " + ex);
                pdfOK = 0;
            }

            doc.open();
            try {

                //Adding logo and topic
                String fileName = imgPath + "POHeader.jpg";

                PdfPTable POHeadertable = new PdfPTable(2);
                POHeadertable.setWidthPercentage(100);
                POHeadertable.setWidths(new int[]{1, 1});
                POHeadertable.addCell(POLogo(fileName));
                POHeadertable.addCell(POHeader("Purchase Order"));
                doc.add(POHeadertable);

                //sup name and po details
                PdfPTable table2 = new PdfPTable(3);
                table2.setWidthPercentage(100);
                float[] widths = {7, 4, 5};
                table2.setWidths(widths);
                table2.addCell(SupName(suppName));
                //get date 
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                table2.addCell(PODetails("Order Date" + '\n' + "PO Code" + '\n' + "Delivery Date" + '\n' + "Retrieve Location" + '\n' + "Supplier Reference"));
                table2.addCell(PODetails(": " + dateFormat.format(date) + '\n' + ": PO0053" + '\n' + ": 20160201" + '\n' + ": OFOffice" + '\n' + ": ALLOCATION PLAN 003"));
                doc.add(table2);

                //po table
                float[] coloumWidths = {7, 2, 2, 2};
                PdfPTable table = new PdfPTable(coloumWidths);
                table.setWidthPercentage(100);
                table.setSpacingBefore(20);

                table.addCell(tableHeaderCellPO("Ingredient Name"));
                table.addCell(tableHeaderCellPO("Unit Price (Rs)"));
                table.addCell(tableHeaderCellPO("Quantity (g)"));
                table.addCell(tableHeaderCellPO("total (Rs)"));

                List lst2 = mainList2.get(count);
                for (int i = 0; i < lst.size(); i++) {
                    table.addCell(tableCellPO(lst.get(i).toString()));
                    table.addCell(tableCellPO(lst2.get(i).toString()));
                }
                doc.add(table);

                //discounts
                PdfPTable discountTable = new PdfPTable(2);
                discountTable.setWidthPercentage(100);
                discountTable.setWidths(new int[]{11, 2});
                discountTable.addCell(discountAndTaxexTopic("Discount (%)"));
                discountTable.addCell(discountAndTaxexData("-   " + discountList.get(count).toString()));
                doc.add(discountTable);

                //taxes
                PdfPTable TaxTable = new PdfPTable(2);
                TaxTable.setWidthPercentage(100);
                TaxTable.setWidths(new int[]{11, 2});
                TaxTable.addCell(discountAndTaxexTopic("Tax (Rs)"));
                TaxTable.addCell(discountAndTaxexData("+   " + taxList.get(count).toString()));
                doc.add(TaxTable);

                //calculate sub total 
                float total = Float.parseFloat(totalList.get(count).toString());
                float discount = Float.parseFloat(discountList.get(count).toString());
                float discountAmount = (total * discount) / 100;
                float tax = Float.parseFloat(taxList.get(count).toString());
                String subTotal = formatNum(total + tax - discountAmount);

                //sub total
                PdfPTable subTotalTable = new PdfPTable(2);
                subTotalTable.setWidthPercentage(100);
                subTotalTable.setWidths(new int[]{11, 2});
                subTotalTable.addCell(discountAndTaxexTopic("Sub Total (Rs)"));
                subTotalTable.addCell(discountAndTaxexData(subTotal));
                doc.add(subTotalTable);

                /*
                 //dilivery charges
                 PdfPTable diliveryChargesTable = new PdfPTable(2);
                 diliveryChargesTable.setWidthPercentage(100);
                 diliveryChargesTable.setWidths(new int[]{9, 2});
                 diliveryChargesTable.addCell(discountAndTaxexTopic("Dilivery Charges"));
                 diliveryChargesTable.addCell(discountAndTaxexData("+   " + "ADD"));
                 doc.add(diliveryChargesTable);
                 */
                //total
                PdfPTable totalTable = new PdfPTable(2);
                totalTable.setWidthPercentage(100);
                totalTable.setWidths(new int[]{11, 2});
                totalTable.addCell(discountAndTaxexTopic("Total (Rs)"));
                totalTable.addCell(discountAndTaxexData(subTotal));
                doc.add(totalTable);

                //company name and date
                PdfPTable companyNameAndDate = new PdfPTable(2);
                companyNameAndDate.setWidthPercentage(100);
                companyNameAndDate.setSpacingBefore(20);
                companyNameAndDate.setWidths(new int[]{5, 1});
                companyNameAndDate.addCell(companyNameAndDate("The Leaf & Spice Company"));

                //powered by
                PdfPTable poweredBy = new PdfPTable(1);
                poweredBy.setWidthPercentage(100);
                poweredBy.setSpacingBefore(5);
                poweredBy.addCell(companyNameAndDate("Powered By : Reid Solutions "));
                doc.add(poweredBy);

                //copyright
                PdfPTable copyRight = new PdfPTable(1);
                copyRight.setWidthPercentage(100);
                copyRight.setSpacingBefore(5);
                copyRight.addCell(companyNameAndDate("\u00a9" + "  2016 Reid Solutions All RIGHTS RESERVED"));
                doc.add(copyRight);

            } catch (DocumentException ex) {
                //Logger.getLogger(OrderConfirmation.class.getName()).log(Level.SEVERE, null, ex);
                logger.log(Level.WARNING, ex.getMessage());
                pdfOK = 0;
            }
            doc.close();
            count++;
        }
        if (pdfOK == 1) {
            return tempPath;
        } else {
            return null;
        }

    }

    public void IngStockHistoryPdfGeneration(JTable table, String date, String dateRange) {
        try {
            Document doc = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);

            //Creating the directory for the order
            String tempPath = path + "Ingredient Stock History\\";
            new File(tempPath).mkdirs();

            PdfWriter.getInstance(doc, new FileOutputStream(tempPath + "Ingredient_Stock_History-" + dateRange + ".pdf"));
            doc.open();

            float[] coloumWidths = {5, 8, 3, 4, 12, 4};
            PdfPTable masterTable = new PdfPTable(coloumWidths);
            masterTable.setWidthPercentage(100);

            //Adding logo
            PdfPCell logoCell = new PdfPCell(Image.getInstance(imgPath + "logo.png"));

            logoCell.setColspan(3);
            masterTable.addCell(logoCell);

            //Adding master plan header data as another table
            coloumWidths = new float[]{5.2f, 5};
            PdfPTable headerTable = new PdfPTable(coloumWidths);
            PdfPCell titleCell = new PdfPCell(new Paragraph("Ingredient Stock History ", FontFactory.getFont(font, 15, Font.BOLD)));
            titleCell.setPadding(15);
            titleCell.setColspan(10);
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerTable.addCell(titleCell);
            headerTable.addCell(getHeaderNameCell("Date taken"));
            headerTable.addCell(getHeaderDataCell(date));
            headerTable.addCell(getHeaderNameCell("Date Range"));
            headerTable.addCell(getHeaderDataCell(dateRange));

            PdfPCell headerDataCell = new PdfPCell(headerTable);
            headerDataCell.setColspan(5);
            masterTable.addCell(headerDataCell);

            //Adding master table headers
            masterTable.addCell(getTableHeaderCell("Date"));
            masterTable.addCell(getTableHeaderCell("Ingredient Name"));
            masterTable.addCell(getTableHeaderCell("Old Qty"));
            masterTable.addCell(getTableHeaderCell("Updated Qty"));
            masterTable.addCell(getTableHeaderCell("Reason"));
            masterTable.addCell(getTableHeaderCell("Updated By"));
            //Adding data from master table

            for (int i = 0; i < table.getRowCount(); i++) {

                masterTable.addCell(getTableDataCell(table.getValueAt(i, 0).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 1).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 2).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 3).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 4).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 5).toString()));
            }
            doc.add(masterTable);

            doc.close();
           
            int response = JOptionPane.showConfirmDialog(
                    null,
                    "PDF generated successfully. Would you like to open the containing folder?",
                    "PSuccess",
                    JOptionPane.YES_NO_OPTION
            );
            if (response == JOptionPane.YES_OPTION) {
                try {
                    //Opening the new directory
                    Desktop.getDesktop().open(new File(tempPath));
                } catch (IOException ex) {
                    logger.log(Level.WARNING, ex.getMessage());
                }
            }
            
        } catch (FileNotFoundException | DocumentException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : PDF 540", "Error", 0);
            System.exit(0);
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : PDF 543", "Error", 0);
            System.exit(0);
        }
    }

    public void BlendStockHistoryPdfGeneration(JTable table, String date, String dateRange) {
        try {
            Document doc = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);

            //Creating the directory for the order
            String tempPath = path + "Blend Stock History\\";
            new File(tempPath).mkdirs();

            PdfWriter.getInstance(doc, new FileOutputStream(tempPath + "Blend_Stock_History-" + dateRange + ".pdf"));
            doc.open();

            float[] coloumWidths = {5, 8, 3, 4, 12, 4};
            PdfPTable masterTable = new PdfPTable(coloumWidths);
            masterTable.setWidthPercentage(100);

            //Adding logo
            PdfPCell logoCell = new PdfPCell(Image.getInstance(imgPath + "logo.png"));

            logoCell.setColspan(3);
            masterTable.addCell(logoCell);

            //Adding master plan header data as another table
            coloumWidths = new float[]{5.2f, 5};
            PdfPTable headerTable = new PdfPTable(coloumWidths);
            PdfPCell titleCell = new PdfPCell(new Paragraph("Blend Stock History ", FontFactory.getFont(font, 15, Font.BOLD)));
            titleCell.setPadding(15);
            titleCell.setColspan(10);
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerTable.addCell(titleCell);
            headerTable.addCell(getHeaderNameCell("Date taken"));
            headerTable.addCell(getHeaderDataCell(date));
            headerTable.addCell(getHeaderNameCell("Date Range"));
            headerTable.addCell(getHeaderDataCell(dateRange));

            PdfPCell headerDataCell = new PdfPCell(headerTable);
            headerDataCell.setColspan(5);
            masterTable.addCell(headerDataCell);

            //Adding master table headers
            masterTable.addCell(getTableHeaderCell("Date"));
            masterTable.addCell(getTableHeaderCell("Blend Name"));
            masterTable.addCell(getTableHeaderCell("Old Qty"));
            masterTable.addCell(getTableHeaderCell("Updated Qty"));
            masterTable.addCell(getTableHeaderCell("Reason"));
            masterTable.addCell(getTableHeaderCell("Updated By"));
            //Adding data from master table

            for (int i = 0; i < table.getRowCount(); i++) {

                masterTable.addCell(getTableDataCell(table.getValueAt(i, 0).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 1).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 2).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 3).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 4).toString()));
                masterTable.addCell(getTableDataCell(table.getValueAt(i, 5).toString()));
            }
            doc.add(masterTable);

            doc.close();

            int response = JOptionPane.showConfirmDialog(
                    null,
                    "PDF generated successfully. Would you like to open the containing folder?",
                    "PSuccess",
                    JOptionPane.YES_NO_OPTION
            );
            if (response == JOptionPane.YES_OPTION) {
                try {
                    //Opening the new directory
                    Desktop.getDesktop().open(new File(tempPath));
                } catch (IOException ex) {
                    logger.log(Level.WARNING, ex.getMessage());
                }
            }
            
        } catch (FileNotFoundException | DocumentException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : PDF 611", "Error", 0);
            System.exit(0);
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : PDF 614", "Error", 0);
            System.exit(0);
        }
    }
}
