import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('ShippingInformation e2e test', () => {
  const shippingInformationPageUrl = '/shipping-information';
  const shippingInformationPageUrlPattern = new RegExp('/shipping-information(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const shippingInformationSample = {};

  let shippingInformation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shipping-informations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shipping-informations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shipping-informations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (shippingInformation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shipping-informations/${shippingInformation.id}`,
      }).then(() => {
        shippingInformation = undefined;
      });
    }
  });

  it('ShippingInformations menu should load ShippingInformations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shipping-information');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ShippingInformation').should('exist');
    cy.url().should('match', shippingInformationPageUrlPattern);
  });

  describe('ShippingInformation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shippingInformationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ShippingInformation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/shipping-information/new$'));
        cy.getEntityCreateUpdateHeading('ShippingInformation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', shippingInformationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shipping-informations',
          body: shippingInformationSample,
        }).then(({ body }) => {
          shippingInformation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shipping-informations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [shippingInformation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(shippingInformationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ShippingInformation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shippingInformation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', shippingInformationPageUrlPattern);
      });

      it('edit button click should load edit ShippingInformation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShippingInformation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', shippingInformationPageUrlPattern);
      });

      it.skip('edit button click should load edit ShippingInformation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShippingInformation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', shippingInformationPageUrlPattern);
      });

      it('last delete button click should delete instance of ShippingInformation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('shippingInformation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', shippingInformationPageUrlPattern);

        shippingInformation = undefined;
      });
    });
  });

  describe('new ShippingInformation page', () => {
    beforeEach(() => {
      cy.visit(`${shippingInformationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ShippingInformation');
    });

    it('should create an instance of ShippingInformation', () => {
      cy.get(`[data-cy="deliveryAddress"]`).type('soda as cleverly');
      cy.get(`[data-cy="deliveryAddress"]`).should('have.value', 'soda as cleverly');

      cy.get(`[data-cy="shippingStatus"]`).type('treasured');
      cy.get(`[data-cy="shippingStatus"]`).should('have.value', 'treasured');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        shippingInformation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', shippingInformationPageUrlPattern);
    });
  });
});
