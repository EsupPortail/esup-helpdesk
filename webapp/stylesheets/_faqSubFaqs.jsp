<%@include file="_include.jsp"%>

<h:panelGroup rendered="#{not empty faqsController.subFaqs or (faqsController.userCanEdit and faqsController.editInterface)}" >
	<t:htmlTag value="hr" />
	<e:bold value="#{msgs['FAQS.TEXT.SUB_FAQS']}" rendered="#{faqsController.userCanEdit and faqsController.editInterface}" />
	<e:panelGrid columns="2" columnClasses="colLeft,colRight" width="100%">
		<h:panelGroup>
			<e:dataTable 
				cellpadding="0" cellspacing="0"
				id="subFaqData" value="#{faqsController.subFaqs}" 
				var="subFaq" rowIndexVar="index" rowCountVar="total"
				rendered="#{not empty faqsController.subFaqs}" >
				<t:column>
					<h:panelGroup style="cursor: pointer; white-space: nowrap" onclick="simulateLinkClick('faqsForm:subFaqData:#{index}:selectFaq');">
						<t:graphicImage value="/media/images/faq#{faqHasChildrenHelper[subFaq]?'-container':''}.png" />
						<e:text value=" #{subFaq.label}" />
					</h:panelGroup>
					<e:commandButton value="->" id="selectFaq" style="display: none" immediate="true" >
						<t:updateActionListener value="#{subFaq}" property="#{faqsController.faq}" />
					</e:commandButton>
				</t:column>
				<t:column>
					<h:panelGroup rendered="#{index != 0 and faqsController.userCanEdit and faqsController.editInterface}" >
						<t:graphicImage value="/media/images/arrow_first.png" 
							alt="^^" title="^^"
							style="cursor: pointer" 
							onclick="simulateLinkClick('faqsForm:subFaqData:#{index}:moveFaqFirst');" />
						<e:commandButton 
							value="^^" id="moveFaqFirst" immediate="true"  
							action="#{faqsController.moveFaqFirst}" style="display: none" >
							<t:updateActionListener value="#{subFaq}" property="#{faqsController.faqToUpdate}" />
						</e:commandButton>
					</h:panelGroup>
				</t:column>
				<t:column>
					<h:panelGroup rendered="#{index != 0 and faqsController.userCanEdit and faqsController.editInterface}" >
						<t:graphicImage value="/media/images/arrow_up.png" 
							alt="^" title="^"
							style="cursor: pointer" 
							onclick="simulateLinkClick('faqsForm:subFaqData:#{index}:moveFaqUp');" />
						<e:commandButton 
							value="^" id="moveFaqUp" immediate="true" rendered="#{index != 0 and faqsController.userCanEdit and faqsController.editInterface}" 
							action="#{faqsController.moveFaqUp}" style="display: none" >
							<t:updateActionListener value="#{subFaq}" property="#{faqsController.faqToUpdate}" />
						</e:commandButton>
					</h:panelGroup>
				</t:column>
				<t:column>
					<h:panelGroup rendered="#{index != total - 1 and faqsController.userCanEdit and faqsController.editInterface}" >
						<t:graphicImage value="/media/images/arrow_down.png" 
							alt="v" title="v"
							style="cursor: pointer" 
							onclick="simulateLinkClick('faqsForm:subFaqData:#{index}:moveFaqDown');" />
						<e:commandButton 
							value="v" id="moveFaqDown" immediate="true"  
							action="#{faqsController.moveFaqDown}" style="display: none" >
							<t:updateActionListener value="#{subFaq}" property="#{faqsController.faqToUpdate}" />
						</e:commandButton>
					</h:panelGroup>
				</t:column>
				<t:column>
					<h:panelGroup rendered="#{index != total - 1 and faqsController.userCanEdit and faqsController.editInterface}" >
						<t:graphicImage value="/media/images/arrow_last.png" 
							style="cursor: pointer" 
							alt="vv" title="vv"
							onclick="simulateLinkClick('faqsForm:subFaqData:#{index}:moveFaqLast');" />
						<e:commandButton 
							value="vv" id="moveFaqLast" immediate="true"  
							action="#{faqsController.moveFaqLast}" style="display: none" >
							<t:updateActionListener value="#{subFaq}" property="#{faqsController.faqToUpdate}" />
						</e:commandButton>
					</h:panelGroup>
				</t:column>
				<t:column>
					<h:panelGroup rendered="#{faqsController.userCanEdit and faqsController.editInterface}" >
						<t:graphicImage value="/media/images/move.png" 
							style="cursor: pointer" 
							alt="->" title="->"
							onclick="simulateLinkClick('faqsForm:subFaqData:#{index}:moveFaq');" />
						<e:commandButton 
							value="->" id="moveFaq" immediate="true"  
							action="#{faqsController.moveFaq}" style="display: none" >
							<t:updateActionListener value="#{subFaq}" property="#{faqsController.faqToUpdate}" />
						</e:commandButton>
					</h:panelGroup>
				</t:column>
			</e:dataTable>
		</h:panelGroup>
		<h:panelGroup>
			<h:panelGroup rendered="#{faqsController.userCanEdit and faqsController.editInterface}" > 
				<h:panelGroup style="cursor: pointer" 
					onclick="simulateLinkClick('faqsForm:addFaqButton');" >
					<e:bold value="#{msgs['FAQS.BUTTON.ADD_FAQ']} " /> 
					<t:graphicImage value="/media/images/add.png" 
						alt="#{msgs['FAQS.BUTTON.ADD_FAQ']}" 
						title="#{msgs['FAQS.BUTTON.ADD_FAQ']}" 
						/>
				</h:panelGroup>
                <e:commandButton 
                    id="addFaqButton" style="display: none"
                    value="#{msgs['FAQS.BUTTON.ADD_FAQ']}" 
                    action="#{faqsController.addFaq}" />
			</h:panelGroup>
		</h:panelGroup>
	</e:panelGrid>
</h:panelGroup>
