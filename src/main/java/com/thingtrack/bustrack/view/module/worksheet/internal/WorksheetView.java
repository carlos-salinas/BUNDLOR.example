package com.thingtrack.bustrack.view.module.worksheet.internal;

import java.util.Date;

import com.thingtrack.bustrack.domain.Worksheet;
import com.thingtrack.bustrack.service.api.WorksheetService;
import com.thingtrack.bustrack.view.web.form.WorksheetViewForm;
import com.thingtrack.konekti.domain.EmployeeAgent;
import com.thingtrack.konekti.domain.Organization;
import com.thingtrack.konekti.service.api.EmployeeAgentService;
import com.thingtrack.konekti.view.addon.data.BindingSource;
import com.thingtrack.konekti.view.addon.ui.AbstractView;
import com.thingtrack.konekti.view.addon.ui.DataGridView;
import com.thingtrack.konekti.view.addon.ui.NavigationToolbar;
import com.thingtrack.konekti.view.addon.ui.WindowDialog;
import com.thingtrack.konekti.view.addon.ui.WindowDialog.DialogResult;
import com.thingtrack.konekti.view.kernel.ui.layout.IViewContainer;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.NestedMethodProperty;
import com.vaadin.event.MouseEvents;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class WorksheetView extends AbstractView {

	@AutoGenerated
	private HorizontalLayout mainLayout;
	@AutoGenerated
	private DataGridView dgDrivers;
	@AutoGenerated
	private InlineDateField inlineDateSelectorField;
	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private EmployeeAgentService employeeAgentService;

	private WorksheetService worksheetService;

	private BindingSource<EmployeeAgent> bsDriver = new BindingSource<EmployeeAgent>(
			EmployeeAgent.class, 0);

	private NavigationToolbar navigationToolbar;

	private IViewContainer viewContainer;

	private Date filteredDate;

	private Organization currentOrganization;

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public WorksheetView(IViewContainer viewContainer) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		filteredDate = new Date();

		inlineDateSelectorField.setImmediate(true);

		inlineDateSelectorField.addListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(Property.ValueChangeEvent event) {

				filteredDate = (Date) event.getProperty().getValue();

			}
		});

		// set Slide View Services and ViewContainer to navigate
		this.viewContainer = viewContainer;

		WorksheetViewContainer worksheetViewContainer = (WorksheetViewContainer) this.viewContainer;
		currentOrganization = worksheetViewContainer.getContext()
				.getOrganization();

		this.employeeAgentService = WorksheetViewContainer
				.getEmployeeAgentService();
		this.worksheetService = WorksheetViewContainer.getWorksheetService();

		dgDrivers.addGeneratedColumn(
				WorksheetAssignmentColumn.WORKSHEET_ASSIGNMENT_COLUMN_ID,
				new WorksheetAssignmentColumn());

		dgDrivers.setColumnAlignment(
				WorksheetAssignmentColumn.WORKSHEET_ASSIGNMENT_COLUMN_ID,
				Table.ALIGN_CENTER);
		
		dgDrivers.setColumnWidth(WorksheetAssignmentColumn.WORKSHEET_ASSIGNMENT_COLUMN_ID, 120);

		// initialize datasource views
		initView();

	}

	private void initView() {
		// initialize Slide View Organization View
		dgDrivers.setImmediate(true);
		dgDrivers.setSelectable(true);

		refreshBindindSource();

		// STEP 01: create grid view for slide Organization View
		try {
			dgDrivers.setBindingSource(bsDriver);
			dgDrivers.setVisibleColumns(new String[] {
					WorksheetAssignmentColumn.WORKSHEET_ASSIGNMENT_COLUMN_ID,
					"workNumber", "name", "surname", "nif" });
			dgDrivers.setColumnHeaders(new String[] { "Parte de trabajo",
					"N�mero Trabajador", "Nombre", "Apellidos", "NIF" });

		} catch (Exception ex) {
			ex.getMessage();
		}

		// STEP 02: create toolbar for slide Organization View
		navigationToolbar = new NavigationToolbar(0, bsDriver, viewContainer);

		removeAllToolbar();

		addToolbar(navigationToolbar);

	}

	private void refreshBindindSource() {
		try {
			bsDriver.removeAllItems();
			bsDriver.addAll(employeeAgentService.getDrivers(currentOrganization));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@AutoGenerated
	private HorizontalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new HorizontalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");

		// inlineDateSelectorField
		inlineDateSelectorField = new InlineDateField();
		inlineDateSelectorField.setImmediate(false);
		inlineDateSelectorField.setWidth("-1px");
		inlineDateSelectorField.setHeight("-1px");
		inlineDateSelectorField.setInvalidAllowed(false);
		inlineDateSelectorField.setResolution(4);
		mainLayout.addComponent(inlineDateSelectorField);

		// dgDrivers
		dgDrivers = new DataGridView();
		dgDrivers.setImmediate(false);
		dgDrivers.setWidth("100.0%");
		dgDrivers.setHeight("100.0%");
		mainLayout.addComponent(dgDrivers);
		mainLayout.setExpandRatio(dgDrivers, 1.0f);

		return mainLayout;
	}

	private class WorksheetAssignmentColumn implements Table.ColumnGenerator {

		static final String WORKSHEET_ASSIGNMENT_COLUMN_ID = "workshhet-asignment-column-id";

		@Override
		public Object generateCell(Table source, Object itemId, Object columnId) {

			final EmployeeAgent selectedDriver = (EmployeeAgent) itemId;

			Embedded worksheetIcon = null;

			try {
				final Worksheet worksheet = worksheetService
						.getWorsheetAssigned(selectedDriver, currentOrganization,
								filteredDate);

				worksheetIcon = new Embedded("", new ThemeResource(
						"images/icons/worksheet-module/inbox-document.png"));
				worksheetIcon.setDescription("Parte de trabajo generado");

				worksheetIcon.addListener(new MouseEvents.ClickListener() {

					@Override
					public void click(MouseEvents.ClickEvent event) {

					}
				});

			} catch (Exception e) {

				worksheetIcon = new Embedded("", new ThemeResource(
						"images/icons/worksheet-module/inbox.png"));

				worksheetIcon.setDescription("No hay parte de trabajo");

				worksheetIcon.addListener(new MouseEvents.ClickListener() {

					@Override
					public void click(MouseEvents.ClickEvent event) {

						Worksheet worksheet = new Worksheet();
						worksheet.setWorksheetStartDate(filteredDate);
						worksheet.setDriver(selectedDriver);
						
						BeanItem<Worksheet> worksheetBeanItem = new BeanItem<Worksheet>(worksheet);
						worksheetBeanItem.addItemProperty("driverName", new NestedMethodProperty(worksheet, "driver.name"));
						worksheetBeanItem.addItemProperty("driverSurname", new NestedMethodProperty(worksheet, "driver.surname"));
						worksheetBeanItem.addItemProperty("driverNif", new NestedMethodProperty(worksheet, "driver.nif"));
						worksheetBeanItem.addItemProperty("driverWorkNumber", new NestedMethodProperty(worksheet, "driver.workNumber"));
						
						Organization organization = new Organization();

						try {
							@SuppressWarnings("unused")
							WindowDialog<Worksheet> windowDialog = new WindowDialog<Worksheet>(getWindow(), "Crear parte de trabajo", 
									"Guardar", DialogResult.SAVE, "Cancelar", DialogResult.CANCEL, 
									new WorksheetViewForm(currentOrganization, WorksheetView.this.filteredDate, selectedDriver), worksheetBeanItem, 
									new WindowDialog.CloseWindowDialogListener<Worksheet>() {
							    public void windowDialogClose(WindowDialog<Worksheet>.CloseWindowDialogEvent<Worksheet> event) {
							    	if (event.getDialogResult() != WindowDialog.DialogResult.SAVE)
							    		return ;
							    	
							    	try {
							    		worksheetService.save(event.getDomainEntity());
									} catch (Exception e) {
										e.printStackTrace();
										
									}
							    }

							});
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
				});

			}

			return worksheetIcon;
		}

	}

}
