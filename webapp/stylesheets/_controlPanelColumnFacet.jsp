<%@include file="_include.jsp"%>

<e:panelGrid columns="1" cellpadding="0" cellspacing="0">
	<h:panelGroup>
         <t:htmlTag value="div" styleClass="column-header">
            <h:panelGroup
                rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'ID'}">
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.ID']} " />
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <h:panelGroup style="cursor:pointer"
                        onclick="#{controlPanelController.firstOrderPartSpec != '+id'} ? buttonClick('controlPanelForm:data:idAscSortButton'): buttonClick('controlPanelForm:data:idDescSortButton');" >
                        <t:htmlTag value="i" styleClass="fas fa-sort"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'DEPARTMENT'}">
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.DEPARTMENT']} " />
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <h:panelGroup style="cursor:pointer"
                        onclick="#{controlPanelController.firstOrderPartSpec != '+department'} ? buttonClick('controlPanelForm:data:departmentAscSortButton'): buttonClick('controlPanelForm:data:departmentDescSortButton');" >
                        <t:htmlTag value="i" styleClass="fas fa-sort"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DEPARTMENT'}">
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.CREATION_DEPARTMENT']} " />
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <h:panelGroup style="cursor:pointer"
                        onclick="#{controlPanelController.firstOrderPartSpec != '+creationDepartment'} ? buttonClick('controlPanelForm:data:creationDepartmentAscSortButton'): buttonClick('controlPanelForm:data:creationDepartmentDescSortButton');" >
                        <t:htmlTag value="i" styleClass="fas fa-sort"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CATEGORY'}">
                <e:text
                    value="#{msgs['CONTROL_PANEL.HEADER.CATEGORY']} " />
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <h:panelGroup style="cursor:pointer"
                        onclick="#{controlPanelController.firstOrderPartSpec != '+category'} ? buttonClick('controlPanelForm:data:categoryAscSortButton'): buttonClick('controlPanelForm:data:categoryDescSortButton');" >
                        <t:htmlTag value="i" styleClass="fas fa-sort"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'LABEL'}">
                <e:text
                    value="#{msgs['CONTROL_PANEL.HEADER.LABEL']} " />
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <h:panelGroup style="cursor:pointer"
                        onclick="#{controlPanelController.firstOrderPartSpec != '+label'} ? buttonClick('controlPanelForm:data:labelAscSortButton'): buttonClick('controlPanelForm:data:labelDescSortButton');" >
                        <t:htmlTag value="i" styleClass="fas fa-sort"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'STATUS'}">
                <e:text
                    value="#{msgs['CONTROL_PANEL.HEADER.STATUS']} " />
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <h:panelGroup style="cursor:pointer"
                        onclick="#{controlPanelController.firstOrderPartSpec != '+status'} ? buttonClick('controlPanelForm:data:statusAscSortButton'): buttonClick('controlPanelForm:data:statusDescSortButton');" >
                        <t:htmlTag value="i" styleClass="fas fa-sort"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'PRIORITY'}">
                <e:text
                    value="#{msgs['CONTROL_PANEL.HEADER.PRIORITY']} " />
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <h:panelGroup style="cursor:pointer"
                        onclick="#{controlPanelController.firstOrderPartSpec != '+priority'} ? buttonClick('controlPanelForm:data:priorityAscSortButton'): buttonClick('controlPanelForm:data:priorityDescSortButton');" >
                        <t:htmlTag value="i" styleClass="fas fa-sort"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DATE' or controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DATE_TIME'}">
                <e:text
                    value="#{msgs['CONTROL_PANEL.HEADER.CREATION_DATE']} " />
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <h:panelGroup style="cursor:pointer"
                        onclick="#{controlPanelController.firstOrderPartSpec != '+id'} ? buttonClick('controlPanelForm:data:idAscSortButton'): buttonClick('controlPanelForm:data:idDescSortButton');" >
                        <t:htmlTag value="i" styleClass="fas fa-sort"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CHANGE_DATE' or controlPanelController.columnsOrderer[columnIndex] == 'CHANGE_DATE_TIME'}">
                <e:text
                    value="#{msgs['CONTROL_PANEL.HEADER.CHANGE_DATE']} " />
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <h:panelGroup style="cursor:pointer"
                        onclick="#{controlPanelController.firstOrderPartSpec != '+lastActionDate'} ? buttonClick('controlPanelForm:data:lastActionDateAscSortButton'): buttonClick('controlPanelForm:data:lastActionDateDescSortButton');" >
                        <t:htmlTag value="i" styleClass="fas fa-sort"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'OWNER'}">
                <e:text
                    value="#{msgs['CONTROL_PANEL.HEADER.OWNER']} " />
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <h:panelGroup style="cursor:pointer"
                        onclick="#{controlPanelController.firstOrderPartSpec != '+owner'} ? buttonClick('controlPanelForm:data:ownerAscSortButton'): buttonClick('controlPanelForm:data:ownerDescSortButton');" >
                        <t:htmlTag value="i" styleClass="fas fa-sort"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup
                rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'MANAGER'}">
                <e:text
                    value="#{msgs['CONTROL_PANEL.HEADER.MANAGER']} " />
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <h:panelGroup style="cursor:pointer"
                        onclick="#{controlPanelController.firstOrderPartSpec != '+manager'} ? buttonClick('controlPanelForm:data:managerAscSortButton'): buttonClick('controlPanelForm:data:managerDescSortButton');" >
                        <t:htmlTag value="i" styleClass="fas fa-sort"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.editColumns}" >
                <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] != null}">
                    <h:panelGroup  rendered="#{controlPanelController.columnsNumber != 1}">
                        <h:panelGroup style="cursor: pointer"
                            onclick="buttonClick('controlPanelForm:data:removeColumn#{columnIndex}Button');" >
                            <t:htmlTag value="i" styleClass="fas fa-trash-alt"/>
                        </h:panelGroup>
                    </h:panelGroup>

                </h:panelGroup>
            </h:panelGroup>

        </t:htmlTag>

		<h:panelGroup styleClass="move-columns" rendered="#{controlPanelController.editColumns}" >
            <h:panelGroup
				rendered="#{controlPanelController.columnsOrderer[columnIndex] != null and controlPanelController.columnsOrderer[columnIndex-1] != null}" >
				<h:panelGroup style="cursor: pointer"
					onclick="buttonClick('controlPanelForm:data:moveColumn#{columnIndex}LeftButton');" >
					 <t:htmlTag value="i" styleClass="fas fa-caret-left"/>
				</h:panelGroup>

			</h:panelGroup>

			<h:panelGroup
				rendered="#{controlPanelController.columnsOrderer[columnIndex] != null}">
				<h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex+1] != null}">
					<h:panelGroup style="cursor: pointer" 
						onclick="buttonClick('controlPanelForm:data:moveColumn#{columnIndex}RightButton');" >
					     <t:htmlTag value="i" styleClass="fas fa-caret-right"/>
					</h:panelGroup>
				</h:panelGroup>


			</h:panelGroup>
		</h:panelGroup>	
	</h:panelGroup>

</e:panelGrid>
