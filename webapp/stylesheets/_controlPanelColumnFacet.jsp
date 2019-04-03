<%@include file="_include.jsp"%>

<e:panelGrid columns="1" cellpadding="0" cellspacing="0">
	<h:panelGroup>
         <t:htmlTag value="div" styleClass="column-header">

            <h:panelGroup rendered="#{controlPanelController.editColumns && controlPanelController.columnsOrderer[columnIndex] == 'ID'}" >
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.ID']}" styleClass="column-label column-numeric"/>
            </h:panelGroup>
            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'ID'}" styleClass="#{(controlPanelController.firstOrderPartSpec == '+id'  or controlPanelController.firstOrderPartSpec == '-id') ? 'selected' : 'not-selected'}">
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <e:text value="#{msgs['CONTROL_PANEL.HEADER.ID']}" styleClass="column-label column-numeric" style="cursor:pointer"
                    onclick="#{controlPanelController.firstOrderPartSpec != '+id'} ? buttonClick('controlPanelForm:data:idAscSortButton'): buttonClick('controlPanelForm:data:idDescSortButton');" />
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '+id'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-numeric-down"/>
                    </h:panelGroup>
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '-id'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-numeric-up"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>


            <h:panelGroup rendered="#{controlPanelController.editColumns && controlPanelController.columnsOrderer[columnIndex] == 'DEPARTMENT'}" >
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.DEPARTMENT']}" styleClass="column-label column-alpha"/>
            </h:panelGroup>
            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'DEPARTMENT'}" styleClass="#{(controlPanelController.firstOrderPartSpec == '+department'  or controlPanelController.firstOrderPartSpec == '-department') ? 'selected' : 'not-selected'}">
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <e:text value="#{msgs['CONTROL_PANEL.HEADER.DEPARTMENT']}" styleClass="column-label column-alpha" style="cursor:pointer"
                    onclick="#{controlPanelController.firstOrderPartSpec != '+department'} ? buttonClick('controlPanelForm:data:departmentAscSortButton'): buttonClick('controlPanelForm:data:departmentDescSortButton');" />
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '+department'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-down"/>
                    </h:panelGroup>
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '-department'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-up"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>


            <h:panelGroup rendered="#{controlPanelController.editColumns && controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DEPARTMENT'}" >
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.CREATION_DEPARTMENT']}" styleClass="column-label column-alpha"/>
            </h:panelGroup>
            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DEPARTMENT'}" styleClass="#{(controlPanelController.firstOrderPartSpec == '+creationDepartment'  or controlPanelController.firstOrderPartSpec == '-creationDepartment') ? 'selected' : 'not-selected'}">
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <e:text value="#{msgs['CONTROL_PANEL.HEADER.CREATION_DEPARTMENT']}" styleClass="column-label column-alpha" style="cursor:pointer"
                    onclick="#{controlPanelController.firstOrderPartSpec != '+creationDepartment'} ? buttonClick('controlPanelForm:data:creationDepartmentAscSortButton'): buttonClick('controlPanelForm:data:creationDepartmentDescSortButton');" />
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '+creationDepartment'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-down"/>
                    </h:panelGroup>
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '-creationDepartment'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-up"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.editColumns && controlPanelController.columnsOrderer[columnIndex] == 'CATEGORY'}" >
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.CATEGORY']}" styleClass="column-label column-alpha"/>
            </h:panelGroup>
            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CATEGORY'}" styleClass="#{(controlPanelController.firstOrderPartSpec == '+category'  or controlPanelController.firstOrderPartSpec == '-category') ? 'selected' : 'not-selected'}">
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <e:text value="#{msgs['CONTROL_PANEL.HEADER.CATEGORY']}" styleClass="column-label column-alpha" style="cursor:pointer"
                    onclick="#{controlPanelController.firstOrderPartSpec != '+category'} ? buttonClick('controlPanelForm:data:categoryAscSortButton'): buttonClick('controlPanelForm:data:categoryDescSortButton');" />
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '+category'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-down"/>
                    </h:panelGroup>
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '-category'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-up"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.editColumns && controlPanelController.columnsOrderer[columnIndex] == 'LABEL'}" >
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.LABEL']}" styleClass="column-label column-alpha"/>
            </h:panelGroup>
            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'LABEL'}" styleClass="#{(controlPanelController.firstOrderPartSpec == '+label'  or controlPanelController.firstOrderPartSpec == '-label') ? 'selected' : 'not-selected'}">
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <e:text value="#{msgs['CONTROL_PANEL.HEADER.LABEL']}" styleClass="column-label column-alpha" style="cursor:pointer"
                    onclick="#{controlPanelController.firstOrderPartSpec != '+label'} ? buttonClick('controlPanelForm:data:labelAscSortButton'): buttonClick('controlPanelForm:data:labelDescSortButton');" />
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '+label'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-down"/>
                    </h:panelGroup>
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '-label'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-up"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.editColumns && controlPanelController.columnsOrderer[columnIndex] == 'STATUS'}" >
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.STATUS']}" styleClass="column-label column-alpha"/>
            </h:panelGroup>
            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'STATUS'}" styleClass="#{(controlPanelController.firstOrderPartSpec == '+status'  or controlPanelController.firstOrderPartSpec == '-status') ? 'selected' : 'not-selected'}">
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <e:text value="#{msgs['CONTROL_PANEL.HEADER.STATUS']}" styleClass="column-label column-alpha" style="cursor:pointer"
                    onclick="#{controlPanelController.firstOrderPartSpec != '+status'} ? buttonClick('controlPanelForm:data:statusAscSortButton'): buttonClick('controlPanelForm:data:statusDescSortButton');" />
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '+status'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-down"/>
                    </h:panelGroup>
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '-status'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-up"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.editColumns && controlPanelController.columnsOrderer[columnIndex] == 'PRIORITY'}" >
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.PRIORITY']}" styleClass="column-label column-alpha"/>
            </h:panelGroup>
            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'PRIORITY'}" styleClass="#{(controlPanelController.firstOrderPartSpec == '+priority'  or controlPanelController.firstOrderPartSpec == '-priority') ? 'selected' : 'not-selected'}">
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <e:text value="#{msgs['CONTROL_PANEL.HEADER.PRIORITY']}" styleClass="column-label column-alpha" style="cursor:pointer"
                    onclick="#{controlPanelController.firstOrderPartSpec != '+priority'} ? buttonClick('controlPanelForm:data:priorityAscSortButton'): buttonClick('controlPanelForm:data:priorityDescSortButton');" />
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '+priority'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-down"/>
                    </h:panelGroup>
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '-priority'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-up"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.editColumns && (controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DATE' or controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DATE_TIME')}" >
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.CREATION_DATE']}" styleClass="column-label column-numeric"/>
            </h:panelGroup>
            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DATE' or controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DATE_TIME'}" styleClass="#{(controlPanelController.firstOrderPartSpec == '+creationDate'  or controlPanelController.firstOrderPartSpec == '-creationDate') ? 'selected' : 'not-selected'}">
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <e:text value="#{msgs['CONTROL_PANEL.HEADER.CREATION_DATE']}" styleClass="column-label column-numeric" style="cursor:pointer"
                    onclick="#{controlPanelController.firstOrderPartSpec != '+creationDate'} ? buttonClick('controlPanelForm:data:creationDateAscSortButton'): buttonClick('controlPanelForm:data:creationDateDescSortButton');" />
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '+creationDate'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-numeric-down"/>
                    </h:panelGroup>
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '-creationDate'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-numeric-up"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>


            <h:panelGroup rendered="#{controlPanelController.editColumns && (controlPanelController.columnsOrderer[columnIndex] == 'CHANGE_DATE' or controlPanelController.columnsOrderer[columnIndex] == 'CHANGE_DATE_TIME')}" >
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.CHANGE_DATE']}" styleClass="column-label column-numeric"/>
            </h:panelGroup>
            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CHANGE_DATE' or controlPanelController.columnsOrderer[columnIndex] == 'CHANGE_DATE_TIME'}" styleClass="#{(controlPanelController.firstOrderPartSpec == '+lastActionDate'  or controlPanelController.firstOrderPartSpec == '-lastActionDate') ? 'selected' : 'not-selected'}">
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <e:text value="#{msgs['CONTROL_PANEL.HEADER.CHANGE_DATE']}" styleClass="column-label column-numeric" style="cursor:pointer"
                    onclick="#{controlPanelController.firstOrderPartSpec != '+lastActionDate'} ? buttonClick('controlPanelForm:data:lastActionDateAscSortButton'): buttonClick('controlPanelForm:data:lastActionDateDescSortButton');" />
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '+lastActionDate'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-numeric-down"/>
                    </h:panelGroup>
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '-lastActionDate'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-numeric-up"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>


            <h:panelGroup rendered="#{controlPanelController.editColumns && controlPanelController.columnsOrderer[columnIndex] == 'OWNER'}" >
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.OWNER']}" styleClass="column-label column-alpha"/>
            </h:panelGroup>
            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'OWNER'}" styleClass="#{(controlPanelController.firstOrderPartSpec == '+owner'  or controlPanelController.firstOrderPartSpec == '-owner') ? 'selected' : 'not-selected'}">
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <e:text value="#{msgs['CONTROL_PANEL.HEADER.OWNER']}" styleClass="column-label column-alpha" style="cursor:pointer"
                    onclick="#{controlPanelController.firstOrderPartSpec != '+owner'} ? buttonClick('controlPanelForm:data:ownerAscSortButton'): buttonClick('controlPanelForm:data:ownerDescSortButton');" />
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '+owner'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-down"/>
                    </h:panelGroup>
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '-owner'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-up"/>
                    </h:panelGroup>
                </h:panelGroup>
            </h:panelGroup>

            <h:panelGroup rendered="#{controlPanelController.editColumns && controlPanelController.columnsOrderer[columnIndex] == 'MANAGER'}" >
                <e:text value="#{msgs['CONTROL_PANEL.HEADER.MANAGER']}" styleClass="column-label column-alpha"/>
            </h:panelGroup>
            <h:panelGroup rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'MANAGER'}" styleClass="#{(controlPanelController.firstOrderPartSpec == '+manager'  or controlPanelController.firstOrderPartSpec == '-manager') ? 'selected' : 'not-selected'}">
                <h:panelGroup rendered="#{not controlPanelController.editColumns}" >
                    <e:text value="#{msgs['CONTROL_PANEL.HEADER.MANAGER']}" styleClass="column-label column-alpha" style="cursor:pointer"
                    onclick="#{controlPanelController.firstOrderPartSpec != '+manager'} ? buttonClick('controlPanelForm:data:managerAscSortButton'): buttonClick('controlPanelForm:data:managerDescSortButton');" />
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '+manager'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-down"/>
                    </h:panelGroup>
                    <h:panelGroup rendered="#{controlPanelController.firstOrderPartSpec == '-manager'}" >
                        <t:htmlTag value="i" styleClass="fas fa-sort-alpha-up"/>
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
