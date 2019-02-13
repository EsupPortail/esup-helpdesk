<%@include file="_include.jsp"%>

<e:panelGrid columns="1" cellpadding="0" cellspacing="0" >
	<h:panelGroup
		style="white-space: nowrap; " >
		<h:panelGroup 
			rendered="#{controlPanelController.editColumns}" >
			<h:panelGroup 
				rendered="#{controlPanelController.columnsOrderer[columnIndex] != null and controlPanelController.columnsOrderer[columnIndex-1] != null}" >
				<h:panelGroup style="cursor: pointer" 
					onclick="simulateLinkClick('controlPanelForm:data:moveColumn#{columnIndex}LeftButton');" >
					<t:graphicImage value="/media/images/move-column-left.png"
						alt="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}" 
						title="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_LEFT']}" />
				</h:panelGroup>
				<e:text value=" " />
			</h:panelGroup>
		</h:panelGroup>
	
		<h:panelGroup 
			rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'ID'}" >
			<e:bold value="#{msgs['CONTROL_PANEL.HEADER.ID']} " />
			<h:panelGroup rendered="#{not controlPanelController.editColumns}" >
				<t:graphicImage value="/media/images/sort-asc#{(controlPanelController.firstOrderPartSpec == '+id') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '+id') ? 'pointer' : ''}" alt="v" title="v"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '+id'}) simulateLinkClick('controlPanelForm:data:idAscSortButton');" />
				<t:graphicImage value="/media/images/sort-desc#{(controlPanelController.firstOrderPartSpec == '-id') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '-id') ? 'pointer' : ''}" alt="^" title="^"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '-id'}) simulateLinkClick('controlPanelForm:data:idDescSortButton');" />
			</h:panelGroup>
		</h:panelGroup>
	
		<h:panelGroup 
			rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'DEPARTMENT'}" >
			<e:bold 
				value="#{msgs['CONTROL_PANEL.HEADER.DEPARTMENT']} " />
			<h:panelGroup rendered="#{not controlPanelController.editColumns}" >
				<t:graphicImage value="/media/images/sort-asc#{(controlPanelController.firstOrderPartSpec == '+department') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '+department') ? 'pointer' : ''}" alt="v" title="v"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '+department'}) simulateLinkClick('controlPanelForm:data:departmentAscSortButton');" />
				<t:graphicImage value="/media/images/sort-desc#{(controlPanelController.firstOrderPartSpec == '-department') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '-department') ? 'pointer' : ''}" alt="^" title="^"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '-department'}) simulateLinkClick('controlPanelForm:data:departmentDescSortButton');" />
			</h:panelGroup>
		</h:panelGroup>
	
		<h:panelGroup 
			rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DEPARTMENT'}" >
			<e:bold 
				value="#{msgs['CONTROL_PANEL.HEADER.CREATION_DEPARTMENT']} " />
			<h:panelGroup rendered="#{not controlPanelController.editColumns}" >
				<t:graphicImage value="/media/images/sort-asc#{(controlPanelController.firstOrderPartSpec == '+creationDepartment') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '+creationDepartment') ? 'pointer' : ''}" alt="v" title="v"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '+creationDepartment'}) simulateLinkClick('controlPanelForm:data:creationDepartmentAscSortButton');" />
				<t:graphicImage value="/media/images/sort-desc#{(controlPanelController.firstOrderPartSpec == '-creationDepartment') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '-creationDepartment') ? 'pointer' : ''}" alt="^" title="^"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '-creationDepartment'}) simulateLinkClick('controlPanelForm:data:creationDepartmentDescSortButton');" />
			</h:panelGroup>
		</h:panelGroup>
	
		<h:panelGroup 
			rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CATEGORY'}" >
			<e:bold 
				value="#{msgs['CONTROL_PANEL.HEADER.CATEGORY']} " />
			<h:panelGroup rendered="#{not controlPanelController.editColumns}" >
				<t:graphicImage value="/media/images/sort-asc#{(controlPanelController.firstOrderPartSpec == '+category') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '+category') ? 'pointer' : ''}" alt="v" title="v"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '+category'}) simulateLinkClick('controlPanelForm:data:categoryAscSortButton');" />
				<t:graphicImage value="/media/images/sort-desc#{(controlPanelController.firstOrderPartSpec == '-category') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '-category') ? 'pointer' : ''}" alt="^" title="^"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '-category'}) simulateLinkClick('controlPanelForm:data:categoryDescSortButton');" />
			</h:panelGroup>
		</h:panelGroup>
		
		<h:panelGroup 
			rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'LABEL'}" >
			<e:bold 
				value="#{msgs['CONTROL_PANEL.HEADER.LABEL']} " />
			<h:panelGroup rendered="#{not controlPanelController.editColumns}" >
				<t:graphicImage value="/media/images/sort-asc#{(controlPanelController.firstOrderPartSpec == '+label') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '+label') ? 'pointer' : ''}" alt="v" title="v"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '+label'}) simulateLinkClick('controlPanelForm:data:labelAscSortButton');" />
				<t:graphicImage value="/media/images/sort-desc#{(controlPanelController.firstOrderPartSpec == '-label') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '-label') ? 'pointer' : ''}" alt="^" title="^"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '-label'}) simulateLinkClick('controlPanelForm:data:labelDescSortButton');" />
			</h:panelGroup>
		</h:panelGroup>
		
		<h:panelGroup 
			rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'STATUS'}" >
			<e:bold 
				value="#{msgs['CONTROL_PANEL.HEADER.STATUS']} " />
			<h:panelGroup rendered="#{not controlPanelController.editColumns}" >
				<t:graphicImage value="/media/images/sort-asc#{(controlPanelController.firstOrderPartSpec == '+status') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '+status') ? 'pointer' : ''}" alt="v" title="v"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '+status'}) simulateLinkClick('controlPanelForm:data:statusAscSortButton');" />
				<t:graphicImage value="/media/images/sort-desc#{(controlPanelController.firstOrderPartSpec == '-status') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '-status') ? 'pointer' : ''}" alt="^" title="^"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '-status'}) simulateLinkClick('controlPanelForm:data:statusDescSortButton');" />
			</h:panelGroup>
		</h:panelGroup>
		
		<h:panelGroup 
			rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'PRIORITY'}" >
			<e:bold 
				value="#{msgs['CONTROL_PANEL.HEADER.PRIORITY']} " />
			<h:panelGroup rendered="#{not controlPanelController.editColumns}" >
				<t:graphicImage value="/media/images/sort-asc#{(controlPanelController.firstOrderPartSpec == '+priority') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '+priority') ? 'pointer' : ''}" alt="v" title="v"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '+priority'}) simulateLinkClick('controlPanelForm:data:priorityAscSortButton');" />
				<t:graphicImage value="/media/images/sort-desc#{(controlPanelController.firstOrderPartSpec == '-priority') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '-priority') ? 'pointer' : ''}" alt="^" title="^"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '-priority'}) simulateLinkClick('controlPanelForm:data:priorityDescSortButton');" />
			</h:panelGroup>
		</h:panelGroup>
		
		<h:panelGroup 
			rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DATE' or controlPanelController.columnsOrderer[columnIndex] == 'CREATION_DATE_TIME'}" >
			<e:bold 
				value="#{msgs['CONTROL_PANEL.HEADER.CREATION_DATE']} " />
			<h:panelGroup rendered="#{not controlPanelController.editColumns}" >
				<t:graphicImage value="/media/images/sort-asc#{(controlPanelController.firstOrderPartSpec == '+id') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '+id') ? 'pointer' : ''}" alt="v" title="v"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '+id'}) simulateLinkClick('controlPanelForm:data:idAscSortButton');" />
				<t:graphicImage value="/media/images/sort-desc#{(controlPanelController.firstOrderPartSpec == '-id') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '-id') ? 'pointer' : ''}" alt="^" title="^"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '-id'}) simulateLinkClick('controlPanelForm:data:idDescSortButton');" />
			</h:panelGroup>
		</h:panelGroup>
		
		<h:panelGroup 
			rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'CHANGE_DATE' or controlPanelController.columnsOrderer[columnIndex] == 'CHANGE_DATE_TIME'}" >
			<e:bold 
				value="#{msgs['CONTROL_PANEL.HEADER.CHANGE_DATE']} " />
			<h:panelGroup rendered="#{not controlPanelController.editColumns}" >
				<t:graphicImage value="/media/images/sort-asc#{(controlPanelController.firstOrderPartSpec == '+lastActionDate') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '+lastActionDate') ? 'pointer' : ''}" alt="v" title="v"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '+lastActionDate'}) simulateLinkClick('controlPanelForm:data:lastActionDateAscSortButton');" />
				<t:graphicImage value="/media/images/sort-desc#{(controlPanelController.firstOrderPartSpec == '-lastActionDate') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '-lastActionDate') ? 'pointer' : ''}" alt="^" title="^"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '-lastActionDate'}) simulateLinkClick('controlPanelForm:data:lastActionDateDescSortButton');" />
			</h:panelGroup>
		</h:panelGroup>
		
		<h:panelGroup 
			rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'OWNER'}" >
			<e:bold 
				value="#{msgs['CONTROL_PANEL.HEADER.OWNER']} " />
			<h:panelGroup rendered="#{not controlPanelController.editColumns}" >
				<t:graphicImage value="/media/images/sort-asc#{(controlPanelController.firstOrderPartSpec == '+owner') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '+owner') ? 'pointer' : ''}" alt="v" title="v"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '+owner'}) simulateLinkClick('controlPanelForm:data:ownerAscSortButton');" />
				<t:graphicImage value="/media/images/sort-desc#{(controlPanelController.firstOrderPartSpec == '-owner') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '-owner') ? 'pointer' : ''}" alt="^" title="^"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '-owner'}) simulateLinkClick('controlPanelForm:data:ownerDescSortButton');" />
			</h:panelGroup>
		</h:panelGroup>
		
		<h:panelGroup 
			rendered="#{controlPanelController.columnsOrderer[columnIndex] == 'MANAGER'}" >
			<e:bold 
				value="#{msgs['CONTROL_PANEL.HEADER.MANAGER']} " />
			<h:panelGroup rendered="#{not controlPanelController.editColumns}" >
				<t:graphicImage value="/media/images/sort-asc#{(controlPanelController.firstOrderPartSpec == '+manager') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '+manager') ? 'pointer' : ''}" alt="v" title="v"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '+manager'}) simulateLinkClick('controlPanelForm:data:managerAscSortButton');" />
				<t:graphicImage value="/media/images/sort-desc#{(controlPanelController.firstOrderPartSpec == '-manager') ? '-selected' : ''}.png" 
					style="cursor: #{(controlPanelController.firstOrderPartSpec != '-manager') ? 'pointer' : ''}" alt="^" title="^"  
					onclick="if (#{controlPanelController.firstOrderPartSpec != '-manager'}) simulateLinkClick('controlPanelForm:data:managerDescSortButton');" />
			</h:panelGroup>
		</h:panelGroup>
	
		<h:panelGroup 
			rendered="#{controlPanelController.editColumns}" >
			<h:panelGroup 
				rendered="#{controlPanelController.columnsOrderer[columnIndex] != null}">
				<h:panelGroup 
					rendered="#{controlPanelController.columnsOrderer[columnIndex+1] != null}">
					<e:text value=" " />
					<h:panelGroup style="cursor: pointer" 
						onclick="simulateLinkClick('controlPanelForm:data:moveColumn#{columnIndex}RightButton');" >
						<t:graphicImage value="/media/images/move-column-right.png"
							alt="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}" 
							title="#{msgs['CONTROL_PANEL.BUTTON.MOVE_COLUMN_RIGHT']}" />
					</h:panelGroup>
				</h:panelGroup>
				<h:panelGroup 
					rendered="#{controlPanelController.columnsNumber != 1}">
					<e:text value=" " />
					<h:panelGroup style="cursor: pointer" 
						onclick="simulateLinkClick('controlPanelForm:data:removeColumn#{columnIndex}Button');" >
						<t:graphicImage value="/media/images/remove-column.png"
							alt="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}" 
							title="#{msgs['CONTROL_PANEL.BUTTON.REMOVE_COLUMN']}" />
					</h:panelGroup>
				</h:panelGroup>
			</h:panelGroup>
		</h:panelGroup>	
	</h:panelGroup>

</e:panelGrid>
