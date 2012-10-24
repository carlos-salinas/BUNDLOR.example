package com.thingtrack.bustrack.view.module.worksheet;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.thingtrack.konekti.view.kernel.IWorkbenchContext;
import com.thingtrack.konekti.view.kernel.ui.layout.AbstractModule;
import com.thingtrack.konekti.view.kernel.ui.layout.IViewContainer;

public class WorksheetModule extends AbstractModule implements BeanFactoryAware {

	private BeanFactory beanFactory;

	private final static String MODULE_NAME = "Worksheet Manager";
	private final static String MODULE_DESCRIPTION = "Worksheet Manager";

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public String getName() {
		return MODULE_NAME;
	}

	@Override
	public String getDescription() {
		return MODULE_DESCRIPTION;
	}

	@Override
	public IViewContainer createViewComponent(IWorkbenchContext context) {

		return (IViewContainer) beanFactory.getBean("worksheetViewContainer",
				context);

	}

}
